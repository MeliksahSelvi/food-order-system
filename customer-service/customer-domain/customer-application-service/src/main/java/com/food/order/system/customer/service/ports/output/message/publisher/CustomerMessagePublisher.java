package com.food.order.system.customer.service.ports.output.message.publisher;

import com.food.order.system.customer.service.common.OutboxStatus;
import com.food.order.system.customer.service.outbox.model.CustomerOutboxMessage;

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
