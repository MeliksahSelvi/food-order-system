package com.food.order.system.payment.service.domain.event;

import com.food.order.system.domain.event.DomainEvent;
import com.food.order.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
 * Payment domain eventlerine ortak kod sağlayan root event
 * */
public abstract class PaymentEvent implements DomainEvent<Payment> {

    private final Payment payment;
    private final ZonedDateTime createdAt;
    private final List<String> failureMessages;

    public PaymentEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        this.payment = payment;
        this.createdAt = createdAt;
        this.failureMessages = failureMessages;
    }

    public Payment getPayment() {
        return payment;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
}
