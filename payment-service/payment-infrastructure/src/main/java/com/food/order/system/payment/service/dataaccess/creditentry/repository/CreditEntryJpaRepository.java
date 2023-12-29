package com.food.order.system.payment.service.dataaccess.creditentry.repository;

import com.food.order.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Repository
public interface CreditEntryJpaRepository extends JpaRepository<CreditEntryEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CreditEntryEntity> findByCustomerId(UUID customerId);
}
