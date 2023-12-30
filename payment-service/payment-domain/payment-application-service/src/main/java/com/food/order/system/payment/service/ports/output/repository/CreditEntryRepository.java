package com.food.order.system.payment.service.ports.output.repository;

import com.food.order.system.payment.service.entity.CreditEntry;
import com.food.order.system.payment.service.valueobject.CustomerId;

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
