package com.food.order.system.payment.service.domain.mapper;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.payment.service.domain.dto.PaymentRequest;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
import com.food.order.system.payment.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

@Component
public class PaymentDataMapper {

    public Payment paymentRequestToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();

    }

    public OrderEventPayload paymentEventToOrderEventPayload(PaymentEvent paymentEvent) {
        return OrderEventPayload.builder()
                .paymentId(paymentEvent.getPayment().getId().getValue().toString())
                .customerId(paymentEvent.getPayment().getCustomerId().getValue().toString())
                .orderId(paymentEvent.getPayment().getOrderId().getValue().toString())
                .price(paymentEvent.getPayment().getPrice().getAmount())
                .createdAt(paymentEvent.getCreatedAt())
                .paymentStatus(paymentEvent.getPayment().getPaymentstatus().name())
                .failureMessages(paymentEvent.getFailureMessages())
                .build();
    }
}
