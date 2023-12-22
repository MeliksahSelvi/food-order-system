package com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.adapter;

import com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.exception.ApprovalOutboxNotFoundException;
import com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.mapper.ApprovalOutboxDataAccessMapper;
import com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.repository.ApprovalOutboxJpaRepository;
import com.food.order.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

/*
 * bu secondary adapter approval outbox entity'sinin output portunu implement ediyor.
 * */
@Component
@RequiredArgsConstructor
public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {

    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;
    private final ApprovalOutboxDataAccessMapper approvalOutboxDataAccessMapper;

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        ApprovalOutboxEntity approvalOutboxEntity = approvalOutboxDataAccessMapper.
                orderApprovalOutboxMessageToApprovalOutboxEntityy(orderApprovalOutboxMessage);

        approvalOutboxEntity = approvalOutboxJpaRepository.save(approvalOutboxEntity);
        return approvalOutboxDataAccessMapper.approvalOutboxEntityToOrderApprovalOutboxMessage(approvalOutboxEntity);
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                                                               OutboxStatus outboxStatus,
                                                                                               SagaStatus... sagaStatuses) {

        return Optional.of(approvalOutboxJpaRepository.findByTypeAndOutbox_statusAndSaga_statusIn(type,
                        outboxStatus, Arrays.asList(sagaStatuses))
                .orElseThrow(() -> new ApprovalOutboxNotFoundException("Approval outbox object " +
                        "could not be found for saga type " + type))
                .stream()
                .map(approvalOutboxDataAccessMapper::approvalOutboxEntityToOrderApprovalOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(String type,
                                                                                   UUID sagaId,
                                                                                   SagaStatus... sagaStatuses) {
        return approvalOutboxJpaRepository.
                findByTypeAndSagaIdaAndSaga_statusIn(type, sagaId, Arrays.asList(sagaStatuses))
                .map(approvalOutboxDataAccessMapper::approvalOutboxEntityToOrderApprovalOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                           OutboxStatus outboxStatus,
                                                           SagaStatus... sagaStatuses) {

        approvalOutboxJpaRepository.deleteByTypeAndOutbox_statusAndSaga_statusIn(type, outboxStatus, Arrays.asList(sagaStatuses));
    }
}
