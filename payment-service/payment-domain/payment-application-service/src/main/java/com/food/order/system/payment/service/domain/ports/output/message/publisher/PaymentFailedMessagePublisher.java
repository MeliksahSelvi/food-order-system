package com.food.order.system.payment.service.domain.ports.output.message.publisher;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.payment.service.domain.event.PaymentFailedEvent;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Bir payment fail olduğunda domain core layer, bu output portu kullanarak PaymentFailedEvent publish edecek.
 * Implementation'u (secondary adapter) payment messaging modülünde yapılacak.
 * */
public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFailedEvent> {
}
