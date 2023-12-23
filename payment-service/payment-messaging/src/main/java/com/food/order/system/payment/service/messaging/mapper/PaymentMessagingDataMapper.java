package com.food.order.system.payment.service.messaging.mapper;

import com.food.order.system.domain.valueobject.PaymentOrderStatus;
import com.food.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.kafka.order.avro.model.PaymentStatus;
import com.food.order.system.payment.service.domain.dto.PaymentRequest;
import com.food.order.system.payment.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
public class PaymentMessagingDataMapper {

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel avroModel) {
        return PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(avroModel.getSagaId())
                .customerId(avroModel.getCustomerId())
                .orderId(avroModel.getOrderId())
                .price(avroModel.getPrice())
                .createdAt(avroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(avroModel.getPaymentOrderStatus().name()))
                .build();
    }

    public PaymentResponseAvroModel orderEventPayloadToPaymentResponseAvroModel(String sagaId, OrderEventPayload payload) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setPaymentId(payload.getPaymentId())
                .setCustomerId(payload.getCustomerId())
                .setOrderId(payload.getOrderId())
                .setPrice(payload.getPrice())
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(payload.getPaymentStatus()))
                .setFailureMessages(payload.getFailureMessages())
                .build();
    }
}
