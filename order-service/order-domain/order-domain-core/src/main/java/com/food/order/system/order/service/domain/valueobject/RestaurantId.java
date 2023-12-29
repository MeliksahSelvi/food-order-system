package com.food.order.system.order.service.domain.valueobject;

import com.food.order.system.order.service.domain.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

public class RestaurantId extends BaseId<UUID> {
    public RestaurantId(UUID value) {
        super(value);
    }
}
