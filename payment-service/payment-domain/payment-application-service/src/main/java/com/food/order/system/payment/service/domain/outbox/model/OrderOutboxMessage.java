package com.food.order.system.payment.service.domain.outbox.model;

import com.food.order.system.payment.service.domain.outbox.common.OutboxStatus;
import com.food.order.system.payment.service.domain.valueobject.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 23.12.2023
 */

/*
 * order outbox tablosunun verileri
 * */
@Getter
@Builder
@AllArgsConstructor
public class OrderOutboxMessage {

    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    private PaymentStatus paymentStatus;
    private OutboxStatus outboxStatus;
    private int version;

    public void setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
    }
}

