package com.food.order.system.dataaccess.customer.mapper;

import com.food.order.system.dataaccess.customer.entity.CustomerOutboxEntity;
import com.food.order.system.outbox.customer.model.CustomerOutboxMessage;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Component
public class CustomerOutboxDataAccessMapper {

    public CustomerOutboxEntity customerOutboxMessageToCustomerOutboxEntity(CustomerOutboxMessage outboxMessage) {
        return CustomerOutboxEntity.builder()
                .id(outboxMessage.getId())
                .sagaId(outboxMessage.getSagaId())
                .type(outboxMessage.getType())
                .payload(outboxMessage.getPayload())
                .createdAt(outboxMessage.getCreatedAt())
                .processedAt(outboxMessage.getProcessedAt())
                .outboxStatus(outboxMessage.getOutboxStatus())
                .version(outboxMessage.getVersion())
                .build();
    }

    public CustomerOutboxMessage customerOutboxEntityToCustomerOutboxMessage(CustomerOutboxEntity outboxEntity) {
        return CustomerOutboxMessage.builder()
                .id(outboxEntity.getId())
                .sagaId(outboxEntity.getSagaId())
                .type(outboxEntity.getType())
                .payload(outboxEntity.getPayload())
                .createdAt(outboxEntity.getCreatedAt())
                .processedAt(outboxEntity.getProcessedAt())
                .outboxStatus(outboxEntity.getOutboxStatus())
                .version(outboxEntity.getVersion())
                .build();
    }
}
