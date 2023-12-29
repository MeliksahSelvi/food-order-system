package com.food.order.system.restaurant.service.domain.event;

import com.food.order.system.restaurant.service.domain.entity.OrderApproval;
import com.food.order.system.restaurant.service.domain.valueobject.RestaurantId;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public class OrderApprovedEvent extends OrderApprovalEvent {
    public OrderApprovedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
    }
}
