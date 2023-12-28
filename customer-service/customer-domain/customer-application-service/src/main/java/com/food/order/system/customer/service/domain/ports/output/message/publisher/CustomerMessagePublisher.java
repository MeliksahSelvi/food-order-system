package com.food.order.system.customer.service.domain.ports.output.message.publisher;

import com.food.order.system.customer.service.domain.outbox.model.CustomerOutboxMessage;
import com.food.order.system.outbox.OutboxStatus;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

/*
 * Customer eklendiği zaman CustomerCreatedEvent publish edileceği zaman bu output portu kullanılıyor.
 * */
public interface CustomerMessagePublisher {
    void publish(CustomerOutboxMessage customerOutboxMessage, BiConsumer<CustomerOutboxMessage, OutboxStatus> outboxCallback);
}
