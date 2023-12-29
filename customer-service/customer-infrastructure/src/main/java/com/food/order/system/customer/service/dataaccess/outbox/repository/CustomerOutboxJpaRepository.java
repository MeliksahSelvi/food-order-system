package com.food.order.system.customer.service.dataaccess.outbox.repository;

import com.food.order.system.customer.service.dataaccess.outbox.entity.CustomerOutboxEntity;
import com.food.order.system.customer.service.common.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 28.12.2023
 */

@Repository
public interface CustomerOutboxJpaRepository extends JpaRepository<CustomerOutboxEntity, UUID> {

    Optional<List<CustomerOutboxEntity>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);
}
