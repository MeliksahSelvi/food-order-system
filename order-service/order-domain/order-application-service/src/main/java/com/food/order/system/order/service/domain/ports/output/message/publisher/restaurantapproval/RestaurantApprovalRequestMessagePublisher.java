package com.food.order.system.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.food.order.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.outbox.OutboxStatus;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

/*
 * Bir order işleminde ödeme yapıldığında Order paid eventi oluşacağı zaman bu outport portu ile publish ediliyor.
 * */
public interface RestaurantApprovalRequestMessagePublisher {

    void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                 BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);
}

