package com.food.order.system.order.service.domain;

import com.food.order.system.domain.valueobject.OrderStatus;
import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.order.service.domain.dto.message.PaymentResponse;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.event.OrderPaidEvent;
import com.food.order.system.order.service.domain.exception.OrderDomainException;
import com.food.order.system.order.service.domain.mapper.OrderDataMapper;
import com.food.order.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
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

/*
 * todo add description
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderDataMapper orderDataMapper;
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
        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(orderPaidEvent.getOrder().getOrderStatus());

        /*
        * bu save işleminde amacımız payment outbox kaydı processing olarak güncelleniyor.
        * ayrıca version arttırılır ve optimistic locking sağlanır.
        * */
        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage,
                orderPaidEvent.getOrder().getOrderStatus(), sagaStatus));
        /* bu save işleminde ise amacımız restaurant service'yi tetikleyecek outbox kaydı oluşturmak
         * ayrıca; type,sagaid ve saga status üzerinde oluşturulan unique index sayesinde
         * aynı anda 2 işlemin aynı kaydı oluşturmasını engellemiş olacağız
         * */
        approvalOutboxHelper.saveApprovalOutboxMessage(orderDataMapper.orderPaidEventToOrderApprovalEventPayload(orderPaidEvent),
                orderPaidEvent.getOrder().getOrderStatus(),
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
}
