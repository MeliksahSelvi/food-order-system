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
 * Payment işlemi complete olduğunda throw edilecek domain event
 * */
public class PaymentCompletedEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher;

    public PaymentCompletedEvent(Payment payment, ZonedDateTime createdAt,
                                 DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.paymentCompletedEventDomainEventPublisher = paymentCompletedEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        this.paymentCompletedEventDomainEventPublisher.publish(this);
    }
}
