package com.food.order.system.order.service.domain.event;

import com.food.order.system.order.service.domain.common.DomainEvent;
import com.food.order.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime createdAt;

    public OrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
