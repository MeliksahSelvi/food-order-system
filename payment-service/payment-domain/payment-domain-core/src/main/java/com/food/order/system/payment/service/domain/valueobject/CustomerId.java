package com.food.order.system.payment.service.domain.valueobject;

import com.food.order.system.payment.service.domain.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

public class CustomerId extends BaseId<UUID> {
    public CustomerId(UUID value) {
        super(value);
    }
}
