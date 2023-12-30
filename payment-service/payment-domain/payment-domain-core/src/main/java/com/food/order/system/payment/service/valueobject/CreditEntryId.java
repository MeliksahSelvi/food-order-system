package com.food.order.system.payment.service.valueobject;

import com.food.order.system.payment.service.common.BaseId;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public class CreditEntryId extends BaseId<UUID> {
    public CreditEntryId(UUID value) {
        super(value);
    }
}
