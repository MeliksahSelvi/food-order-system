package com.food.order.system.order.service.ports.output.message.publisher.restaurantapproval;

import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.approval.OrderApprovalOutboxMessage;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

/*
 * Bir order işleminde ödeme yapıldığında Order paid eventi oluşacağı zaman bu outport portu ile publish ediliyor.
 * */
public interface ApprovalRequestMessagePublisher {

    void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                 BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);
}

