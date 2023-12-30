package com.food.order.system.user.service.valueobject;

import com.food.order.system.user.service.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

public class UserId extends BaseId<UUID> {
    public UserId(UUID value) {
        super(value);
    }
}
