package com.food.order.system.order.service.domain.outbox.scheduler.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.domain.valueobject.OrderStatus;
import com.food.order.system.order.service.domain.exception.OrderDomainException;
import com.food.order.system.domain.event.payload.OrderApprovalEventPayload;
import com.food.order.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.food.order.system.saga.order.SagaConstants.ORDER_SAGA_NAME;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalOutboxHelper {

    private final ApprovalOutboxRepository approvalOutboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>> getApprovalOutboxMessageByOutboxStatusAndSagaStatuses(OutboxStatus outboxStatus,
                                                                                                            SagaStatus... sagaStatuses) {

        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage> getApprovalOutboxMessageBySagaIdAndSagaStatuses(UUID sagaId,
                                                                                                SagaStatus... sagaStatuses) {

        return approvalOutboxRepository.findByTypeAndSagaIdAndSagaStatuses(ORDER_SAGA_NAME, sagaId, sagaStatuses);
    }

    @Transactional
    public void save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        OrderApprovalOutboxMessage response = approvalOutboxRepository.save(orderApprovalOutboxMessage);
        /*
         * Eğer saga stepinin güncel durumunu save ederken bir sorun oluşursa hemen durumu handle ediyoruz.
         * */
        if (response == null) {
            log.error("Could not save OrderApprovalOutboxMessage with outbox id: {}", orderApprovalOutboxMessage.getId());
            throw new OrderDomainException("Could not save OrderApprovalOutboxMessage with outbox id: " +
                    orderApprovalOutboxMessage.getId());
        }
        log.info("OrderApprovalOutboxMessage saved with outbox id: {}", orderApprovalOutboxMessage.getId());
    }

    @Transactional
    public void persistApprovalOutboxMessage(OrderApprovalEventPayload approvalEventPayload, OrderStatus orderStatus,
                                             SagaStatus sagaStatus, OutboxStatus outboxStatus, UUID sagaId) {

        save(OrderApprovalOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(approvalEventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(approvalEventPayload))
                .orderStatus(orderStatus)
                .outboxStatus(outboxStatus)
                .sagaStatus(sagaStatus)
                .build());
    }

    @Transactional
    public void deleteApprovalOutboxMessageByOutboxStatusAndSagaStatuses(OutboxStatus outboxStatus,
                                                                         SagaStatus... sagaStatuses) {

        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

    private String createPayload(OrderApprovalEventPayload approvalEventPayload) {
        try {
            return objectMapper.writeValueAsString(approvalEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderApprovalEventPayload object for order id: {}", approvalEventPayload.getOrderId(), e);
            throw new OrderDomainException("Could not create OrderApprovalEventPayload object for order id: " +
                    approvalEventPayload.getOrderId(), e);
        }
    }
}
