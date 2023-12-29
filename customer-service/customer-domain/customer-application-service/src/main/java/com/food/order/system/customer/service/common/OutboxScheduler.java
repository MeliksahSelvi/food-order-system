package com.food.order.system.customer.service.common;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

/*
 * outbox tabloları üzerinde işlem yapan tüm scheduler için marker interface
 * */
public interface OutboxScheduler {
    void processOutboxMessage();
}
