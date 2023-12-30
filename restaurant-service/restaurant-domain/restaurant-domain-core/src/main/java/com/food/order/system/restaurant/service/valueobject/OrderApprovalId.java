package com.food.order.system.restaurant.service.valueobject;


import com.food.order.system.restaurant.service.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public class OrderApprovalId extends BaseId<UUID> {
    public OrderApprovalId(UUID value) {
        super(value);
    }
}
