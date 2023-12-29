package com.food.order.system.order.service.dataaccess.outbox.payment.entity;

import com.food.order.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.order.service.domain.valueobject.OrderStatus;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_outbox")
public class PaymentOutboxEntity {

    @Id
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    @Version
    private int version;

    public OrderPaymentOutboxMessage toModel() {
        return OrderPaymentOutboxMessage.builder()
                .id(id)
                .sagaId(sagaId)
                .createdAt(createdAt)
                .processedAt(processedAt)
                .type(type)
                .payload(payload)
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .version(version)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentOutboxEntity that = (PaymentOutboxEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
