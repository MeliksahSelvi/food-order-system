package com.food.order.system.order.service.event;

import com.food.order.system.order.service.entity.Order;

import java.time.ZonedDateTime;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class OrderPaidEvent extends OrderEvent {
    public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
