package com.food.order.system.order.service.valueobject;

import com.food.order.system.order.service.common.BaseId;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}
