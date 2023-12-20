package com.food.order.system.payment.service.domain.event;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
 * Payment işlemi cancel olduğunda throw edilecek domain event
 * */
public class PaymentCancelledEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher;

    public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt,
                                 DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.paymentCancelledEventDomainEventPublisher = paymentCancelledEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        this.paymentCancelledEventDomainEventPublisher.publish(this);
    }
}
