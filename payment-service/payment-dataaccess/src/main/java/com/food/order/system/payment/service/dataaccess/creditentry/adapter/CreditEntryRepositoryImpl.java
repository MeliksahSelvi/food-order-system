package com.food.order.system.payment.service.dataaccess.creditentry.adapter;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import com.food.order.system.payment.service.dataaccess.creditentry.mapper.CreditEntryDataAccessMapper;
import com.food.order.system.payment.service.dataaccess.creditentry.repository.CreditEntryJpaRepository;
import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
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
    private final CreditEntryDataAccessMapper creditEntryDataAccessMapper;


    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        CreditEntryEntity creditEntryEntity = creditEntryDataAccessMapper.creditEntryToCreditEntryEntity(creditEntry);
        creditEntryEntity=creditEntryJpaRepository.save(creditEntryEntity);
        return creditEntryDataAccessMapper.creditEntryEntityToCreditEntry(creditEntryEntity);
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
        Optional<CreditEntryEntity> creditEntryEntityOptional = creditEntryJpaRepository.findByCustomerId(customerId.getValue());
        return creditEntryEntityOptional.map(creditEntryDataAccessMapper::creditEntryEntityToCreditEntry);
    }
}
