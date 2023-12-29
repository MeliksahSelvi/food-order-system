package com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.adapter;

import com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.exception.ApprovalOutboxNotFoundException;
import com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.repository.ApprovalOutboxJpaRepository;
import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.order.service.ports.output.repository.ApprovalOutboxRepository;
import com.food.order.system.order.service.saga.SagaStatus;
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

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        ApprovalOutboxEntity approvalOutboxEntity = ApprovalOutboxEntity.builder()
                .id(orderApprovalOutboxMessage.getId())
                .sagaId(orderApprovalOutboxMessage.getSagaId())
                .createdAt(orderApprovalOutboxMessage.getCreatedAt())
                .type(orderApprovalOutboxMessage.getType())
                .payload(orderApprovalOutboxMessage.getPayload())
                .orderStatus(orderApprovalOutboxMessage.getOrderStatus())
                .sagaStatus(orderApprovalOutboxMessage.getSagaStatus())
                .outboxStatus(orderApprovalOutboxMessage.getOutboxStatus())
                .version(orderApprovalOutboxMessage.getVersion())
                .build();

        return approvalOutboxJpaRepository.save(approvalOutboxEntity).toModel();
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                                                               OutboxStatus outboxStatus,
                                                                                               SagaStatus... sagaStatuses) {

        return Optional.of(approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type,
                        outboxStatus, Arrays.asList(sagaStatuses))
                .orElseThrow(() -> new ApprovalOutboxNotFoundException("Approval outbox object " +
                        "could not be found for saga type " + type))
                .stream()
                .map(ApprovalOutboxEntity::toModel)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(String type,
                                                                                   UUID sagaId,
                                                                                   SagaStatus... sagaStatuses) {
        return approvalOutboxJpaRepository.
                findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatuses))
                .map(ApprovalOutboxEntity::toModel);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                           OutboxStatus outboxStatus,
                                                           SagaStatus... sagaStatuses) {

        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.asList(sagaStatuses));
    }
}
