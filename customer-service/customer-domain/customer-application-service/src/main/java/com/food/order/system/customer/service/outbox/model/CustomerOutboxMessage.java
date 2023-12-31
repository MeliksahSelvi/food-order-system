package com.food.order.system.customer.service.outbox.model;

import com.food.order.system.customer.service.common.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 28.12.2023
 */

/*
 * customer outbox tablosunun verileri
 * */
@Getter
@Builder
@AllArgsConstructor
public class CustomerOutboxMessage {
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    private OutboxStatus outboxStatus;
    private int version;

    public void setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
    }
}
