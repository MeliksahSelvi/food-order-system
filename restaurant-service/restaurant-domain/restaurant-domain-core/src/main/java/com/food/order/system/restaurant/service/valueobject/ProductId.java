package com.food.order.system.restaurant.service.valueobject;

import com.food.order.system.restaurant.service.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

public class ProductId extends BaseId<UUID> {
    public ProductId(UUID value) {
        super(value);
    }
}