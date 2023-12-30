package com.food.order.system.restaurant.service.event;

import com.food.order.system.restaurant.service.entity.OrderApproval;
import com.food.order.system.restaurant.service.valueobject.RestaurantId;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public class OrderRejectedEvent extends OrderApprovalEvent {
    public OrderRejectedEvent(OrderApproval orderApproval, RestaurantId restaurantId, List<String> failureMessages, ZonedDateTime createdAt) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
    }
}
