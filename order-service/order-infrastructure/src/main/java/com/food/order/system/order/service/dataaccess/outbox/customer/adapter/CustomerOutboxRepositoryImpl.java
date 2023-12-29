package com.food.order.system.order.service.dataaccess.outbox.customer.adapter;

import com.food.order.system.order.service.dataaccess.outbox.customer.entity.CustomerOutboxEntity;
import com.food.order.system.order.service.dataaccess.outbox.customer.exception.CustomerOutboxNotFoundException;
import com.food.order.system.order.service.dataaccess.outbox.customer.repository.CustomerOutboxJpaRepository;
import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.customer.CustomerOutboxMessage;
import com.food.order.system.order.service.ports.output.repository.CustomerOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * customer_outbox tablosunun secondary adapterı
 * */
@Component
@RequiredArgsConstructor
public class CustomerOutboxRepositoryImpl implements CustomerOutboxRepository {
    private final CustomerOutboxJpaRepository customerOutboxJpaRepository;

    @Override
    public CustomerOutboxMessage save(CustomerOutboxMessage orderOutboxMessage) {
        CustomerOutboxEntity customerOutboxEntity = CustomerOutboxEntity.builder()
                .id(orderOutboxMessage.getId())
                .sagaId(orderOutboxMessage.getSagaId())
                .type(orderOutboxMessage.getType())
                .payload(orderOutboxMessage.getPayload())
                .createdAt(orderOutboxMessage.getCreatedAt())
                .processedAt(orderOutboxMessage.getProcessedAt())
                .outboxStatus(orderOutboxMessage.getOutboxStatus())
                .version(orderOutboxMessage.getVersion())
                .build();
        return customerOutboxJpaRepository.save(customerOutboxEntity).toModel();
    }

    @Override
    public Optional<List<CustomerOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        return Optional.of(customerOutboxJpaRepository.findByTypeAndOutboxStatus(type, outboxStatus)
                .orElseThrow(() -> new CustomerOutboxNotFoundException("Customer outbox object " +
                        "cannot be found for saga type " + type))
                .stream()
                .map(CustomerOutboxEntity::toModel)
                .collect(Collectors.toList()));
    }

    @Override
    public void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        customerOutboxJpaRepository.deleteByTypeAndOutboxStatus(type, outboxStatus);
    }
}
