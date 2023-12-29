package com.food.order.system.restaurant.service.domain.valueobject;

import com.food.order.system.restaurant.service.domain.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

public class OrderId extends BaseId<UUID> {
    public OrderId(UUID value) {
        super(value);
    }
}
