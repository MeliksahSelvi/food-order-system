package com.food.order.system.order.service.outbox.scheduler.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.order.service.common.DomainConstants;
import com.food.order.system.order.service.exception.OrderDomainException;
import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.customer.CustomerEventPayload;
import com.food.order.system.order.service.outbox.model.customer.CustomerOutboxMessage;
import com.food.order.system.order.service.ports.output.repository.CustomerOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return customerOutboxRepository.findByTypeAndOutboxStatus(DomainConstants.CUSTOMER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void save(CustomerOutboxMessage orderOutboxMessage) {
        CustomerOutboxMessage response = customerOutboxRepository.save(orderOutboxMessage);
        if (response == null) {
            log.error("Could not save CustomerOutboxMessage!");
            throw new OrderDomainException("Could not save CustomerOutboxMessage!");
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
                .type(DomainConstants.CUSTOMER_SAGA_NAME)
                .outboxStatus(outboxStatus)
                .payload(createPayload(customerEventPayload))
                .createdAt(customerEventPayload.getCreatedAt())
                .processedAt(ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)))
                .build());
    }

    @Transactional
    public void deleteByOutboxStatus(OutboxStatus outboxStatus) {
        customerOutboxRepository.deleteByTypeAndOutboxStatus(DomainConstants.CUSTOMER_SAGA_NAME, outboxStatus);
    }

    private String createPayload(CustomerEventPayload customerEventPayload) {
        try {
            return objectMapper.writeValueAsString(customerEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create CustomerEventPayload json!", e);
            throw new OrderDomainException("Could not create CustomerEventPayload json!", e);
        }
    }
}
