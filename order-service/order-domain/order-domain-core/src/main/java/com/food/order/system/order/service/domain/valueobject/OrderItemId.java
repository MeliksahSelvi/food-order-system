package com.food.order.system.order.service.domain.valueobject;

import com.food.order.system.domain.valueobject.BaseId;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class OrderItemId extends BaseId<Long> {
    protected OrderItemId(Long value) {
        super(value);
    }
}
