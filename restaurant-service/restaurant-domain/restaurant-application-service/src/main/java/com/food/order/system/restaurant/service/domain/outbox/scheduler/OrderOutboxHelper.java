package com.food.order.system.restaurant.service.domain.outbox.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.restaurant.service.domain.exception.RestaurantDomainException;
import com.food.order.system.restaurant.service.domain.outbox.common.OutboxStatus;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.order.system.restaurant.service.domain.ports.output.repository.OrderOutboxRepository;
import com.food.order.system.restaurant.service.domain.valueobject.OrderApprovalStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.food.order.system.restaurant.service.domain.constants.DomainConstants.ORDER_SAGA_NAME;
import static com.food.order.system.restaurant.service.domain.constants.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxHelper {

    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        return orderOutboxRepository.findByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional(readOnly = true)
    public Optional<OrderOutboxMessage> getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(
            UUID sagaId, OutboxStatus outboxStatus) {

        return orderOutboxRepository.findByTypeAndSagaIdAndOutboxStatus(ORDER_SAGA_NAME, sagaId, outboxStatus);
    }

    @Transactional
    public void save(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxMessage response = orderOutboxRepository.save(orderOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderOutboxMessage!");
            throw new RestaurantDomainException("Could not save OrderOutboxMessage!");
        }
        log.info("OrderOutboxMessage saved with outbox id: {}", orderOutboxMessage.getId());
    }

    @Transactional
    public void persistOrderOutboxMessage(OrderEventPayload orderEventPayload,
                                          OrderApprovalStatus orderApprovalStatus,
                                          OutboxStatus outboxStatus,
                                          UUID sagaId) {
        save(OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .type(ORDER_SAGA_NAME)
                .outboxStatus(outboxStatus)
                .orderApprovalStatus(orderApprovalStatus)
                .payload(createPayload(orderEventPayload))
                .createdAt(orderEventPayload.getCreatedAt())
                .processedAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .build());
    }

    @Transactional
    public void deleteByOutboxStatus(OutboxStatus outboxStatus) {
        orderOutboxRepository.deleteByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void updateOutboxStatus(OrderOutboxMessage orderOutboxMessage, OutboxStatus outboxStatus) {
        orderOutboxMessage.setOutboxStatus(outboxStatus);
        save(orderOutboxMessage);
        log.info("OrderOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }

    private String createPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderEventPayload json!", e);
            throw new RestaurantDomainException("Could not create OrderEventPayload json!", e);
        }
    }
}
