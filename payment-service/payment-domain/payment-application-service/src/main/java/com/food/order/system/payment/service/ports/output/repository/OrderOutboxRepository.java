package com.food.order.system.payment.service.ports.output.repository;

import com.food.order.system.payment.service.outbox.common.OutboxStatus;
import com.food.order.system.payment.service.outbox.model.OrderOutboxMessage;
import com.food.order.system.payment.service.valueobject.PaymentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 23.12.2023
 */
/*
 * order_outbox tablosunun output portu
 * */
public interface OrderOutboxRepository {

    OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage);

    Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type,
                                                                                    UUID sagaId,
                                                                                    PaymentStatus paymentStatus,
                                                                                    OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);
}
