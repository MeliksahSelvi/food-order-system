package com.food.order.system.order.service.dataaccess.outbox.payment.repository;

import com.food.order.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
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
public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutboxEntity, UUID> {

    Optional<List<PaymentOutboxEntity>> findByTypeAndOutbox_statusAndSaga_statusIn(String type,
                                                                                   OutboxStatus outboxStatus,
                                                                                   List<SagaStatus> sagaStatuses);

    Optional<PaymentOutboxEntity> findByTypeAndSagaIdaAndSaga_statusIn(String type,
                                                                       UUID sagaId,
                                                                       List<SagaStatus> sagaStatuses);

    void deleteByTypeAndOutbox_statusAndSaga_statusIn(String type,
                                                      OutboxStatus outboxStatus,
                                                      List<SagaStatus> sagaStatuses);
}
