package com.food.order.system.payment.service.domain.ports.output.repository;

import com.food.order.system.payment.service.domain.entity.CreditHistory;
import com.food.order.system.payment.service.domain.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * CreditHistory entity'sinin output portu
 * */
public interface CreditHistoryRepository {

    CreditHistory save(CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId);
}
