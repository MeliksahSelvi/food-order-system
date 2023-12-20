package com.food.order.system.order.service.domain.event;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class OrderCancelledEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCancelledEvent> orderCancelledEventDomainEventPublisher;

    public OrderCancelledEvent(Order order, ZonedDateTime createdAt,
                               DomainEventPublisher<OrderCancelledEvent> orderCancelledEventDomainEventPublisher) {
        super(order, createdAt);
        this.orderCancelledEventDomainEventPublisher = orderCancelledEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        this.orderCancelledEventDomainEventPublisher.publish(this);
    }
}
