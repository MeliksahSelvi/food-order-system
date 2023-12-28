package com.food.order.system.order.service.domain;

import com.food.order.system.domain.valueobject.OrderStatus;
import com.food.order.system.domain.valueobject.PaymentOrderStatus;
import com.food.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.event.OrderCancelledEvent;
import com.food.order.system.order.service.domain.exception.OrderDomainException;
import com.food.order.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.order.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.order.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.food.order.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
import com.food.order.system.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.food.order.system.domain.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 21.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;

    @Override
    @Transactional
    public void process(RestaurantApprovalResponse restaurantApprovalResponse) {
        Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessagesResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatuses(
                        UUID.fromString(restaurantApprovalResponse.getSagaId()),
                        SagaStatus.PROCESSING);

        if (orderApprovalOutboxMessagesResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed!",
                    restaurantApprovalResponse.getSagaId());
            return;
        }

        OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessagesResponse.get();

        Order order = approveOrder(restaurantApprovalResponse);
        OrderStatus orderStatus = order.getOrderStatus();
        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(orderStatus);

        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(orderApprovalOutboxMessage,
                orderStatus, sagaStatus));
        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(restaurantApprovalResponse.getSagaId(),
                orderStatus, sagaStatus));
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    @Transactional
    public void rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessagesResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatuses(
                        UUID.fromString(restaurantApprovalResponse.getSagaId()),
                        SagaStatus.PROCESSING);

        if (orderApprovalOutboxMessagesResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already roll backed!",
                    restaurantApprovalResponse.getSagaId());
            return;
        }

        OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessagesResponse.get();

        OrderCancelledEvent orderCancelledEvent = rollbackOrder(restaurantApprovalResponse);

        OrderStatus orderStatus = orderCancelledEvent.getOrder().getOrderStatus();
        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(orderStatus);

        //approval outbox kaydı compensating olarak güncelleniyor.
        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(orderApprovalOutboxMessage,
                orderStatus, sagaStatus));

        //payment işlemi iptal edilmesi için yeni payment outbox kaydı
        paymentOutboxHelper.savePaymentOutboxMessage(createOrderPaymentEventPayload(orderCancelledEvent),
                orderStatus,
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(restaurantApprovalResponse.getSagaId()));

        log.info("Order with id: {} is cancelling", orderCancelledEvent.getOrder().getId().getValue());
    }

    private Order approveOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
        String orderId = restaurantApprovalResponse.getOrderId();
        log.info("Approving order with id: {}", orderId);
        Order order = orderSagaHelper.findOrder(orderId);
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        return order;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                                                                       OrderStatus orderStatus,
                                                                       SagaStatus sagaStatus) {
        orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderApprovalOutboxMessage.setOrderStatus(orderStatus);
        orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
        return orderApprovalOutboxMessage;
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(String sagaId, OrderStatus orderStatus, SagaStatus sagaStatus) {
        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessagesResponse =
                paymentOutboxHelper.getOutboxMessageBySagaIdAndSagaStatuses(UUID.fromString(sagaId), SagaStatus.PROCESSING);

        if (orderPaymentOutboxMessagesResponse.isEmpty()) {
            throw new OrderDomainException("Payment outbox message could not be found in " +
                    SagaStatus.PROCESSING.name() + " status!");
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessagesResponse.get();
        orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return orderPaymentOutboxMessage;
    }

    private OrderCancelledEvent rollbackOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
        String orderId = restaurantApprovalResponse.getOrderId();
        log.info("Cancelling order with id: {}", orderId);
        Order order = orderSagaHelper.findOrder(orderId);
        OrderCancelledEvent orderCancelledEvent = orderDomainService.cancelOrderPayment(order, restaurantApprovalResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        return orderCancelledEvent;
    }

    private OrderPaymentEventPayload createOrderPaymentEventPayload(OrderCancelledEvent orderCancelledEvent) {
        return OrderPaymentEventPayload.builder()
                .customerId(orderCancelledEvent.getOrder().getCustomerId().getValue().toString())
                .orderId(orderCancelledEvent.getOrder().getId().getValue().toString())
                .price(orderCancelledEvent.getOrder().getPrice().getAmount())
                .createdAt(orderCancelledEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.CANCELLED.name())
                .build();
    }
}
