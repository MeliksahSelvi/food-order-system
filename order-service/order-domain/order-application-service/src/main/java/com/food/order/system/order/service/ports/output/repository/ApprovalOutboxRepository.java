package com.food.order.system.order.service.ports.output.repository;

import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.order.service.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */
/*
 * OrderApprovalOutboxMessage output portu
 * */
public interface ApprovalOutboxRepository {

    OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage);

    Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                                                        OutboxStatus outboxStatus,
                                                                                        SagaStatus... sagaStatuses);

    Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(String type,
                                                                            UUID sagaId,
                                                                            SagaStatus... sagaStatuses);

    void deleteByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                    OutboxStatus outboxStatus,
                                                    SagaStatus... sagaStatuses);

}
