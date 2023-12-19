package com.food.order.system.payment.service.domain.event;

import com.food.order.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public class PaymentCompletedEvent extends PaymentEvent{
    public PaymentCompletedEvent(Payment payment, ZonedDateTime createdAt) {
        super(payment, createdAt, Collections.emptyList());
    }
}
