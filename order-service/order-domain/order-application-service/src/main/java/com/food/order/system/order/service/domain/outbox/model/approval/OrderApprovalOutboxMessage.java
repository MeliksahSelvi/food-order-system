package com.food.order.system.order.service.domain.outbox.model.approval;

import com.food.order.system.order.service.domain.valueobject.OrderStatus;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

/*
 * restauran_approval_outbox tablosunun verileri
 * */
@Getter
@Builder
@AllArgsConstructor
public class OrderApprovalOutboxMessage {
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    private SagaStatus sagaStatus;
    private OutboxStatus outboxStatus;
    private OrderStatus orderStatus;
    private int version;

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public void setSagaStatus(SagaStatus sagaStatus) {
        this.sagaStatus = sagaStatus;
    }

    public void setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
