package com.food.order.system.payment.service.domain.valueobject;

import com.food.order.system.payment.service.domain.common.BaseId;

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
