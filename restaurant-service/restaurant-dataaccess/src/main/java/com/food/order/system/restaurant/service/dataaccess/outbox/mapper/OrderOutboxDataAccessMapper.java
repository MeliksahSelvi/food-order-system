package com.food.order.system.restaurant.service.dataaccess.outbox.mapper;

import com.food.order.system.restaurant.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Component
public class OrderOutboxDataAccessMapper {

    public OrderOutboxEntity orderOutBoxMessageToOrderOutBoxEntity(OrderOutboxMessage message){
        return OrderOutboxEntity.builder()
                .id(message.getId())
                .sagaId(message.getSagaId())
                .type(message.getType())
                .payload(message.getPayload())
                .orderApprovalStatus(message.getOrderApprovalStatus())
                .outboxStatus(message.getOutboxStatus())
                .createdAt(message.getCreatedAt())
                .processedAt(message.getProcessedAt())
                .version(message.getVersion())
                .build();
    }

    public OrderOutboxMessage orderOutboxEntityToOrderOutboxMessage(OrderOutboxEntity entity){
        return OrderOutboxMessage.builder()
                .id(entity.getId())
                .sagaId(entity.getSagaId())
                .type(entity.getType())
                .payload(entity.getPayload())
                .orderApprovalStatus(entity.getOrderApprovalStatus())
                .outboxStatus(entity.getOutboxStatus())
                .createdAt(entity.getCreatedAt())
                .processedAt(entity.getProcessedAt())
                .version(entity.getVersion())
                .build();
    }
}
