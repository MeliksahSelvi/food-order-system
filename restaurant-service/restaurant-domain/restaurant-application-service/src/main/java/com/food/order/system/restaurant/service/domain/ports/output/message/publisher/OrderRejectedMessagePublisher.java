package com.food.order.system.restaurant.service.domain.ports.output.message.publisher;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.restaurant.service.domain.event.OrderRejectedEvent;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Bir order reject edildiğinde domain core layer, bu output portu kullanarak OrderRejectedEvent publish edecek.
 * Implementation'u (secondary adapter) restaurant messaging modülünde yapılacak.
 * */
public interface OrderRejectedMessagePublisher extends DomainEventPublisher<OrderRejectedEvent> {
}
