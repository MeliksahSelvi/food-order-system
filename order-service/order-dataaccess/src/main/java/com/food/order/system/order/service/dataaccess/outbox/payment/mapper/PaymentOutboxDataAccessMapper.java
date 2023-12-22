package com.food.order.system.order.service.dataaccess.outbox.payment.mapper;

import com.food.order.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.order.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

@Component
public class PaymentOutboxDataAccessMapper {

    public PaymentOutboxEntity orderPaymentOutboxMessageToPaymentOutboxEntity(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return PaymentOutboxEntity.builder()
                .id(orderPaymentOutboxMessage.getId())
                .sagaId(orderPaymentOutboxMessage.getSagaId())
                .createdAt(orderPaymentOutboxMessage.getCreatedAt())
                .type(orderPaymentOutboxMessage.getType())
                .payload(orderPaymentOutboxMessage.getPayload())
                .order_status(orderPaymentOutboxMessage.getOrderStatus())
                .saga_status(orderPaymentOutboxMessage.getSagaStatus())
                .outbox_status(orderPaymentOutboxMessage.getOutboxStatus())
                .version(orderPaymentOutboxMessage.getVersion())
                .build();
    }

    public OrderPaymentOutboxMessage paymentOutboxEntityToOrderPaymentOutboxMessage(PaymentOutboxEntity paymentOutboxEntity) {
        return OrderPaymentOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaId(paymentOutboxEntity.getSagaId())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .type(paymentOutboxEntity.getType())
                .payload(paymentOutboxEntity.getPayload())
                .orderStatus(paymentOutboxEntity.getOrder_status())
                .sagaStatus(paymentOutboxEntity.getSaga_status())
                .outboxStatus(paymentOutboxEntity.getOutbox_status())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }
}
