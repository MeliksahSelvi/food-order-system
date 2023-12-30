package com.food.order.system.payment.service.dataaccess.outbox.entity;

import com.food.order.system.payment.service.outbox.common.OutboxStatus;
import com.food.order.system.payment.service.outbox.model.OrderOutboxMessage;
import com.food.order.system.payment.service.valueobject.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 23.12.2023
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_outbox")
public class OrderOutboxEntity {

    @Id
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    @Version
    private int version;

    public OrderOutboxMessage toModel() {
        return OrderOutboxMessage.builder()
                .id(id)
                .sagaId(sagaId)
                .createdAt(createdAt)
                .processedAt(processedAt)
                .type(type)
                .payload(payload)
                .outboxStatus(outboxStatus)
                .paymentStatus(paymentStatus)
                .version(version)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderOutboxEntity that = (OrderOutboxEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
