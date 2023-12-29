package com.food.order.system.restaurant.service.domain.valueobject;

import com.food.order.system.restaurant.service.domain.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

//todo belkide bu id'leri burada tutmak yerine json ile string yapsak olur mu?
public class OrderId extends BaseId<UUID> {
    public OrderId(UUID value) {
        super(value);
    }
}
