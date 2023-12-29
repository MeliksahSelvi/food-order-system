package com.food.order.system.payment.service.dataaccess.creditentry.adapter;

import com.food.order.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import com.food.order.system.payment.service.dataaccess.creditentry.repository.CreditEntryJpaRepository;
import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.order.system.payment.service.domain.valueobject.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * CreditEntry entity'sinin secondary adapter'ı
 * */
@Component
@RequiredArgsConstructor
public class CreditEntryRepositoryImpl implements CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        CreditEntryEntity creditEntryEntity = CreditEntryEntity.builder()
                .id(creditEntry.getId().getValue())
                .customerId(creditEntry.getCustomerId().getValue())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().getAmount())
                .build();
        return creditEntryJpaRepository.save(creditEntryEntity).toModel();
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
        return creditEntryJpaRepository.findByCustomerId(customerId.getValue()).map(CreditEntryEntity::toModel);
    }
}
