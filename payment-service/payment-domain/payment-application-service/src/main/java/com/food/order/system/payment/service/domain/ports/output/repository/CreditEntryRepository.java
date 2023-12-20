package com.food.order.system.payment.service.domain.ports.output.repository;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.payment.service.domain.entity.CreditEntry;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
 * CreditEntry entity'sinin output portu
 * */
public interface CreditEntryRepository {

    CreditEntry save(CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}