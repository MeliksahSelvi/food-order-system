package com.food.order.system.order.service.outbox.model.payment;

import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.saga.SagaStatus;
import com.food.order.system.order.service.valueobject.OrderStatus;
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
 * payment outbox tablosunun verileri
 * */
@Getter
@Builder
@AllArgsConstructor
public class OrderPaymentOutboxMessage {
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
