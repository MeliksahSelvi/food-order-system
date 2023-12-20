package com.food.order.system.payment.service.dataaccess.creditentry.mapper;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.payment.service.domain.valueobject.CreditEntryId;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
public class CreditEntryDataAccessMapper {

    public CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
        return CreditEntry.builder()
                .creditEntryId(new CreditEntryId(creditEntryEntity.getId()))
                .customerId(new CustomerId(creditEntryEntity.getCustomerId()))
                .totalCreditAmount(new Money(creditEntryEntity.getTotalCreditAmount()))
                .build();
    }

    public CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry) {
        return CreditEntryEntity.builder()
                .id(creditEntry.getId().getValue())
                .customerId(creditEntry.getCustomerId().getValue())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().getAmount())
                .build();
    }
}
