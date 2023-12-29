package com.food.order.system.customer.service.ports.output.repository;

import com.food.order.system.customer.service.common.OutboxStatus;
import com.food.order.system.customer.service.outbox.model.CustomerOutboxMessage;

import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * customer_outbox tablosunun output portu
 * */
public interface CustomerOutboxRepository {

    CustomerOutboxMessage save(CustomerOutboxMessage orderOutboxMessage);

    Optional<List<CustomerOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);
}
