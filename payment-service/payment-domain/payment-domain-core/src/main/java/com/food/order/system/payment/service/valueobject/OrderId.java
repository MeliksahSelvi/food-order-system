package com.food.order.system.payment.service.valueobject;

import com.food.order.system.payment.service.common.BaseId;

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