package com.food.order.system.order.service.ports.output.message.publisher.payment;

import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.payment.OrderPaymentOutboxMessage;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

/*
 * Order create ve order cancel eventleri oluşacağı zaman bu outport portu ile publish ediliyor.
 * */
public interface PaymentRequestMessagePublisher {

    void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);
}
