package com.food.order.system.payment.service.domain.ports.output.message.publisher;

import com.food.order.system.payment.service.domain.outbox.common.OutboxStatus;
import com.food.order.system.payment.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

/*
 * Order işleminde PaymentCompletedEvent,PaymentCancelledEvent and PaymentFailedEvent publish edileceği zaman bu output portu kullanılıyor.
 * */
public interface PaymentResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
