package com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.repository;

import com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

@Repository
public interface ApprovalOutboxJpaRepository extends JpaRepository<ApprovalOutboxEntity, UUID> {

    Optional<List<ApprovalOutboxEntity>> findByTypeAndOutbox_statusAndSaga_statusIn(String type,
                                                                                    OutboxStatus outboxStatus,
                                                                                    List<SagaStatus> sagaStatuses);

    Optional<ApprovalOutboxEntity> findByTypeAndSagaIdaAndSaga_statusIn(String type,
                                                                        UUID sagaId,
                                                                        List<SagaStatus> sagaStatuses);

    void deleteByTypeAndOutbox_statusAndSaga_statusIn(String type,
                                                      OutboxStatus outboxStatus,
                                                      List<SagaStatus> sagaStatuses);
}
