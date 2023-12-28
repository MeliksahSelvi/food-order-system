package com.food.order.system.customer.service.domain.outbox.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.customer.service.domain.exception.CustomerDomainException;
import com.food.order.system.customer.service.domain.outbox.model.CustomerEventPayload;
import com.food.order.system.customer.service.domain.outbox.model.CustomerOutboxMessage;
import com.food.order.system.customer.service.domain.ports.output.repository.CustomerOutboxRepository;
import com.food.order.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.food.order.system.domain.DomainConstants.UTC;
import static com.food.order.system.saga.order.SagaConstants.CUSTOMER_SAGA_NAME;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerOutboxHelper {

    private final CustomerOutboxRepository customerOutboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<List<CustomerOutboxMessage>> getCustomerOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        return customerOutboxRepository.findByTypeAndOutboxStatus(CUSTOMER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void save(CustomerOutboxMessage orderOutboxMessage) {
        CustomerOutboxMessage response = customerOutboxRepository.save(orderOutboxMessage);
        if (response == null) {
            log.error("Could not save CustomerOutboxMessage!");
            throw new CustomerDomainException("Could not save CustomerOutboxMessage!");
        }
        log.info("CustomerOutboxMessage saved with outbox id: {}", orderOutboxMessage.getId());
    }

    @Transactional
    public void persistCustomerOutboxMessage(CustomerEventPayload customerEventPayload,
                                             OutboxStatus outboxStatus,
                                             UUID sagaId) {
        save(CustomerOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .type(CUSTOMER_SAGA_NAME)
                .outboxStatus(outboxStatus)
                .payload(createPayload(customerEventPayload))
                .createdAt(customerEventPayload.getCreatedAt())
                .processedAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .build());
    }

    @Transactional
    public void deleteByOutboxStatus(OutboxStatus outboxStatus) {
        customerOutboxRepository.deleteByTypeAndOutboxStatus(CUSTOMER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void updateOutboxStatus(CustomerOutboxMessage customerOutboxMessage, OutboxStatus outboxStatus) {
        customerOutboxMessage.setOutboxStatus(outboxStatus);
        save(customerOutboxMessage);
        log.info("CustomerOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }

    private String createPayload(CustomerEventPayload customerEventPayload) {
        try {
            return objectMapper.writeValueAsString(customerEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create CustomerEventPayload json!", e);
            throw new CustomerDomainException("Could not create CustomerEventPayload json!", e);
        }
    }
}
