package com.food.order.system.payment.service.messaging.mapper;

import com.food.order.system.domain.event.payload.OrderPaymentEventPayload;
import com.food.order.system.domain.valueobject.PaymentOrderStatus;
import com.food.order.system.payment.service.domain.dto.PaymentRequest;
import debezium.order.payment_outbox.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
public class PaymentMessagingDataMapper {

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(OrderPaymentEventPayload orderPaymentEventPayload,
                                                                  Value avroModel) {
        return PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(avroModel.getSagaId())
                .customerId(orderPaymentEventPayload.getCustomerId())
                .orderId(orderPaymentEventPayload.getOrderId())
                .price(orderPaymentEventPayload.getPrice())
                .createdAt(Instant.parse(avroModel.getCreatedAt()))
                .paymentOrderStatus(PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                .build();
    }

}
