package com.food.order.system.payment.service.domain.ports.output.message.publisher;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.payment.service.domain.event.PaymentCancelledEvent;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Bir payment cancelled edildiğinde domain core layer, bu output portu kullanarak PaymentCancelledEvent publish edecek.
 * Implementation'u (secondary adapter) payment messaging modülünde yapılacak.
 * */
public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent> {
}
