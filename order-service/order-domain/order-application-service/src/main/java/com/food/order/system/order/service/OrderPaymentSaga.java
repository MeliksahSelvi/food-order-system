package com.food.order.system.order.service;

import com.food.order.system.order.service.dto.message.PaymentResponse;
import com.food.order.system.order.service.entity.Order;
import com.food.order.system.order.service.event.OrderPaidEvent;
import com.food.order.system.order.service.exception.OrderDomainException;
import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.approval.OrderApprovalEventPayload;
import com.food.order.system.order.service.outbox.model.approval.OrderApprovalEventProduct;
import com.food.order.system.order.service.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.order.service.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.order.service.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.food.order.system.order.service.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.order.system.order.service.saga.SagaStatus;
import com.food.order.system.order.service.saga.SagaStep;
import com.food.order.system.order.service.valueobject.OrderStatus;
import com.food.order.system.order.service.valueobject.PaymentStatus;
import com.food.order.system.order.service.valueobject.RestaurantOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.food.order.system.order.service.constants.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 21.12.2023
 */


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

    private final OrderDomainService orderDomainService;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;

    @Override
    @Transactional
    public void process(PaymentResponse paymentResponse) {
        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessagesResponse =
                paymentOutboxHelper.getOutboxMessageBySagaIdAndSagaStatuses(
                        UUID.fromString(paymentResponse.getSagaId()),
                        SagaStatus.STARTED);
        /*
         * Payment service'den 2 tane mesaj gelirse 2.si bu if clause ile return olur. 2 mesaj gelme sebebi şunlar olabilir:
         * 1- order service payment'e 2 kere ödeme talebi göndermiş olabilir.
         * 2- scheduler outboxmessage tamamlandı olarak işaretlenmeden 2 kere çalışmış olabilir.
         * 3- order service'nin birden fazla instance'si olduğu zaman aynı mesaj her örnekten kafkaya gönderilmiş olabilir.
         * Kısacası distributed system'lerde duplicate hataları olabilir.
         * */
        if (orderPaymentOutboxMessagesResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed!", paymentResponse.getSagaId());
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessagesResponse.get();
        OrderPaidEvent orderPaidEvent = completePaymentForOrder(paymentResponse);
        OrderStatus orderStatus = orderPaidEvent.getOrder().getOrderStatus();
        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(orderStatus);

        /*
         * bu save işleminde amacımız payment outbox kaydı processing olarak güncelleniyor.
         * ayrıca bu save işlemi ile version arttırılır ve optimistic locking sağlanır.
         * bu lock sayesinde bu methoda giren 2.thread, methodun başındaki if clause'ye takılması sağlanır.
         * */
        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage, orderStatus, sagaStatus));
        /* bu save işleminde ise amacımız restaurant service'yi tetikleyecek outbox kaydı oluşturmak
         * ayrıca; type,sagaid ve saga status üzerinde oluşturulan unique index sayesinde
         * aynı anda 2 işlemin aynı kaydı oluşturmasını engellemiş olacağız
         * */
        approvalOutboxHelper.persistApprovalOutboxMessage(createOrderApprovalEventPayload(orderPaidEvent),
                orderStatus,
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(paymentResponse.getSagaId()));

        log.info("Order with id: {} is paid", orderPaidEvent.getOrder().getId().getValue());
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {

        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessagesResponse =
                paymentOutboxHelper.getOutboxMessageBySagaIdAndSagaStatuses(
                        UUID.fromString(paymentResponse.getSagaId()),
                        getCurrentSagaStatus(paymentResponse.getPaymentStatus()));

        if (orderPaymentOutboxMessagesResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already roll backed!", paymentResponse.getSagaId());
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessagesResponse.get();

        Order order = rollbackPaymentForOrder(paymentResponse);
        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getOrderStatus());

        /*
         * bu save işleminde amacımız payment outbox kaydı STARTED veya COMPENSATED olarak güncelleniyor.
         * ayrıca version arttırılır ve optimistic locking sağlanır.
         * */
        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage, order.getOrderStatus(), sagaStatus));


        /*
         * Cancelled ise approval için durum güncellemesi yapılmalı.
         * */
        if (paymentResponse.getPaymentStatus() == PaymentStatus.CANCELLED) {
            approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(paymentResponse.getSagaId(),
                    order.getOrderStatus(), sagaStatus));
        }

        log.info("Order with id: {} is cancelled", paymentResponse.getOrderId());
    }


    private OrderPaidEvent completePaymentForOrder(PaymentResponse paymentResponse) {
        String orderId = paymentResponse.getOrderId();
        log.info("Completing payment for order with id: {}", orderId);
        Order order = orderSagaHelper.findOrder(orderId);
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order);
        orderSagaHelper.saveOrder(order);
        return orderPaidEvent;
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                                                                     OrderStatus orderStatus, SagaStatus sagaStatus) {

        orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return orderPaymentOutboxMessage;
    }

    private SagaStatus[] getCurrentSagaStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            //sadece ödeme alınmışsa hala started düzeyindedir.
            case COMPLETED -> new SagaStatus[]{SagaStatus.STARTED};
            //ödeme alınmış ve approval işlemi başlatılmış ise processing'dedir.
            case CANCELLED -> new SagaStatus[]{SagaStatus.PROCESSING};
            //ödeme alınamamaış veya ödeme alınmış approval işlemi başlatılmış olabilir.
            case FAILED -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }

    private Order rollbackPaymentForOrder(PaymentResponse paymentResponse) {
        log.info("Cancelling order with id: {}", paymentResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        return order;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(String sagaId,
                                                                       OrderStatus orderStatus,
                                                                       SagaStatus sagaStatus) {
        //cancel olduğu zaman bu işlem öncesinde approval_outbox tablosundaki ilgili kaydın sagastatus'u compensating'e çekilmiş durumda
        Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatuses(UUID.fromString(sagaId), SagaStatus.COMPENSATING);

        if (orderApprovalOutboxMessageResponse.isEmpty()) {
            throw new OrderDomainException("Approval outbox message could not be found in " +
                    SagaStatus.COMPENSATING.name() + " status!");
        }

        OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();
        orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderApprovalOutboxMessage.setOrderStatus(orderStatus);
        orderApprovalOutboxMessage.setSagaStatus(sagaStatus);

        return orderApprovalOutboxMessage;
    }

    private OrderApprovalEventPayload createOrderApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
        return OrderApprovalEventPayload.builder()
                .orderId(orderPaidEvent.getOrder().getId().getValue().toString())
                .restaurantId(orderPaidEvent.getOrder().getRestaurantId().getValue().toString())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID.name())
                .products(orderPaidEvent.getOrder().getItems().stream().map(orderItem ->
                        OrderApprovalEventProduct.builder()
                                .id(orderItem.getProduct().getId().getValue().toString())
                                .quantity(orderItem.getQuantity())
                                .build()).collect(Collectors.toList()))
                .price(orderPaidEvent.getOrder().getPrice().getAmount())
                .createdAt(orderPaidEvent.getCreatedAt())
                .build();
    }
}
