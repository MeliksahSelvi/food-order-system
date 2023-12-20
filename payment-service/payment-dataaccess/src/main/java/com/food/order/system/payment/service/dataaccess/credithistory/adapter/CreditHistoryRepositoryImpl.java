package com.food.order.system.payment.service.dataaccess.credithistory.adapter;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import com.food.order.system.payment.service.dataaccess.credithistory.mapper.CreditHistoryDataAccessMapper;
import com.food.order.system.payment.service.dataaccess.credithistory.repository.CreditHistoryJpaRepository;
import com.food.order.system.payment.service.domain.entity.CreditHistory;
import com.food.order.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * CreditHistory entity'sinin secondary adapter'ı
 * */
@Component
@RequiredArgsConstructor
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;
    private final CreditHistoryDataAccessMapper creditHistoryDataAccessMapper;

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        CreditHistoryEntity creditHistoryEntity = creditHistoryDataAccessMapper.creditHistoryToCreditHistoryEntity(creditHistory);
        creditHistoryEntity = creditHistoryJpaRepository.save(creditHistoryEntity);
        return creditHistoryDataAccessMapper.creditHistoryEntityToCreditHistory(creditHistoryEntity);
    }

    @Override
    public Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId) {
        Optional<List<CreditHistoryEntity>> historyOptional = creditHistoryJpaRepository.findByCustomerId(customerId.getValue());
        return historyOptional.map(creditHistoryEntities ->
                creditHistoryEntities.stream()
                        .map(creditHistoryDataAccessMapper::creditHistoryEntityToCreditHistory)
                        .collect(Collectors.toList()));
    }
}
