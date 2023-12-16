package com.food.order.system.order.service.domain.ports.output.message.publisher.payment;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.order.service.domain.event.OrderCancelledEvent;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Bir order cancelled edildiğinde domain core layer, bu output portu kullanarak OrderCancelledEvent publish edecek.
 * Implementation'u (secondary adapter) order messaging modülünde yapılacak.
 * */
public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
