package com.food.order.system.payment.service.domain.valueobject;


import com.food.order.system.payment.service.domain.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public class CreditHistoryId extends BaseId<UUID> {
    public CreditHistoryId(UUID value) {
        super(value);
    }
}
