package com.food.order.system.payment.service.dataaccess.credithistory.adapter;

import com.food.order.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import com.food.order.system.payment.service.dataaccess.credithistory.repository.CreditHistoryJpaRepository;
import com.food.order.system.payment.service.entity.CreditHistory;
import com.food.order.system.payment.service.ports.output.repository.CreditHistoryRepository;
import com.food.order.system.payment.service.valueobject.CustomerId;
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

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        CreditHistoryEntity creditHistoryEntity = CreditHistoryEntity.builder()
                .id(creditHistory.getId().getValue())
                .customerId(creditHistory.getCustomerId().getValue())
                .amount(creditHistory.getAmount().getAmount())
                .transactionType(creditHistory.getTransactionType())
                .build();
        return creditHistoryJpaRepository.save(creditHistoryEntity).toModel();
    }

    @Override
    public Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId) {
        return creditHistoryJpaRepository.findByCustomerId(customerId.getValue())
                .map(creditHistoryEntities ->
                        creditHistoryEntities.stream()
                                .map(CreditHistoryEntity::toModel)
                                .collect(Collectors.toList()));
    }
}
