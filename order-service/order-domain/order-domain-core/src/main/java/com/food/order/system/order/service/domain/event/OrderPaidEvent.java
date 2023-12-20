package com.food.order.system.order.service.domain.event;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class OrderPaidEvent extends OrderEvent {

    private final DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher;

    public OrderPaidEvent(Order order, ZonedDateTime createdAt,
                          DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher) {
        super(order, createdAt);
        this.orderPaidEventDomainEventPublisher = orderPaidEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        this.orderPaidEventDomainEventPublisher.publish(this);
    }
}
