package com.food.order.system.restaurant.service.domain.ports.output.message.publisher;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * Approval işleminde OrderApprovedEvent ve OrderRejectedEvent publish edileceği zaman bu output portu kullanılıyor.
 * */
public interface RestaurantApprovalResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
