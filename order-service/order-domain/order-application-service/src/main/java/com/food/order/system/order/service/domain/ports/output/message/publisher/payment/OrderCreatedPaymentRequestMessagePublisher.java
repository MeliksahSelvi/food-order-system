package com.food.order.system.order.service.domain.ports.output.message.publisher.payment;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.order.service.domain.event.OrderCreatedEvent;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Bir order create edildiğinde domain core layer, bu output portu kullanarak OrderCreatedEvent publish edecek.
 * Implementation'u (secondary adapter) order messaging modülünde yapılacak.
 * */
public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
