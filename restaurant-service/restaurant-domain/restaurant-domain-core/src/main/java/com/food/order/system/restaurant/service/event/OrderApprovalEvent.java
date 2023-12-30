package com.food.order.system.restaurant.service.event;

import com.food.order.system.restaurant.service.common.DomainEvent;
import com.food.order.system.restaurant.service.entity.OrderApproval;
import com.food.order.system.restaurant.service.valueobject.RestaurantId;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public abstract class OrderApprovalEvent implements DomainEvent<OrderApproval> {
    private final OrderApproval orderApproval;
    private final RestaurantId restaurantId;
    private final List<String> failureMessages;
    private final ZonedDateTime createdAt;

    public OrderApprovalEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt) {
        this.orderApproval = orderApproval;
        this.restaurantId = restaurantId;
        this.failureMessages = failureMessages;
        this.createdAt = createdAt;
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
