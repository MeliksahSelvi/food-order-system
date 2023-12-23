package com.food.order.system.payment.service.domain.event;

import com.food.order.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
 * Payment işlemi fail olduğunda throw edilecek domain event
 * */
public class PaymentFailedEvent extends PaymentEvent {
    public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        super(payment, createdAt, failureMessages);
    }
}
