package com.food.order.system.restaurant.service.domain.ports.output.message.publisher;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.restaurant.service.domain.event.OrderApprovedEvent;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Bir order approve edildiğinde domain core layer, bu output portu kullanarak OrderApprovedEvent publish edecek.
 * Implementation'u (secondary adapter) restaurant messaging modülünde yapılacak.
 * */
public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {
}
