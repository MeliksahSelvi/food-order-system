package com.food.order.system.payment.service.messaging.mapper;

import com.food.order.system.domain.valueobject.PaymentOrderStatus;
import com.food.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.kafka.order.avro.model.PaymentStatus;
import com.food.order.system.payment.service.domain.dto.PaymentRequest;
import com.food.order.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.order.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
import com.food.order.system.payment.service.domain.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
public class PaymentMessagingDataMapper {

    public PaymentResponseAvroModel paymentCompletedEventToPaymentResponseAvroModel(PaymentCompletedEvent paymentCompletedEvent) {
        return buildPaymentResponseAvroModel(paymentCompletedEvent);
    }

    public PaymentResponseAvroModel paymentCancelledEventToPaymentResponseAvroModel(PaymentCancelledEvent paymentCancelledEvent) {
        return buildPaymentResponseAvroModel(paymentCancelledEvent);
    }

    public PaymentResponseAvroModel paymentFailedEventToPaymentResponseAvroModel(PaymentFailedEvent paymentFailedEvent) {
        return buildPaymentResponseAvroModel(paymentFailedEvent);
    }

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentRequestAvroModel) {
        return PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaId("")
                .customerId(paymentRequestAvroModel.getCustomerId())
                .orderId(paymentRequestAvroModel.getOrderId())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().name()))
                .build();
    }

    private PaymentResponseAvroModel buildPaymentResponseAvroModel(PaymentEvent paymentEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentEvent.getPayment().getCustomerId().getValue().toString())
                .setOrderId(paymentEvent.getPayment().getOrderId().getValue().toString())
                .setPrice(paymentEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentEvent.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(paymentEvent.getPayment().getPaymentstatus().name()))
                .setFailureMessages(paymentEvent.getFailureMessages())
                .build();
    }

}
