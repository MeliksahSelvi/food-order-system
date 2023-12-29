package com.food.order.system.order.service.valueobject;

import com.food.order.system.order.service.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID value) {
        super(value);
    }
}
