package com.food.order.system.restaurant.service.domain.ports.output.message.publisher;

import com.food.order.system.restaurant.service.domain.outbox.common.OutboxStatus;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

/*
 * Approval işleminde OrderApprovedEvent ve OrderRejectedEvent publish edileceği zaman bu output portu kullanılıyor.
 * */
public interface ApprovalResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
