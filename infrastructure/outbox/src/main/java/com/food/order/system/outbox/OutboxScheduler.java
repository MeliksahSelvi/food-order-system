package com.food.order.system.outbox;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

/*
* outbox tabloları üzerinde işlem yapan tüm scheduler için marker interface
 * */
public interface OutboxScheduler {
    void processOutboxMessage();
}
