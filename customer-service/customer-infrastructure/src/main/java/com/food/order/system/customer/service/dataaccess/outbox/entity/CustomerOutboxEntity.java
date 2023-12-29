package com.food.order.system.customer.service.dataaccess.outbox.entity;

import com.food.order.system.customer.service.common.OutboxStatus;
import com.food.order.system.customer.service.outbox.model.CustomerOutboxMessage;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 28.12.2023
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_outbox")
public class CustomerOutboxEntity {

    @Id
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    @Version
    private int version;

    public CustomerOutboxMessage toModel() {
        return CustomerOutboxMessage.builder()
                .id(id)
                .sagaId(sagaId)
                .type(type)
                .payload(payload)
                .createdAt(createdAt)
                .processedAt(processedAt)
                .outboxStatus(outboxStatus)
                .version(version)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerOutboxEntity that = (CustomerOutboxEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
