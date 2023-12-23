package com.food.order.system.restaurant.service.domain.ports.output.repository;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * order_outbox tablosunun output portu
 * */
public interface OrderOutboxRepository {

    OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage);

    Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    Optional<OrderOutboxMessage> findByTypeAndSagaIdAndOutboxStatus(String type,
                                                                    UUID sagaId,
                                                                    OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);
}