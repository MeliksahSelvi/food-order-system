package com.food.order.system.payment.service.event;

import com.food.order.system.payment.service.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
 * Payment işlemi complete olduğunda throw edilecek domain event
 * */
public class PaymentCompletedEvent extends PaymentEvent {
    public PaymentCompletedEvent(Payment payment, ZonedDateTime createdAt) {
        super(payment, createdAt, Collections.emptyList());
    }
}
