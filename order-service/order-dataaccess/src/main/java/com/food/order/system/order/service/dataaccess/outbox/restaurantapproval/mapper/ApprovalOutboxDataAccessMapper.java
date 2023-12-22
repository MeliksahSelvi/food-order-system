package com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.mapper;

import com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.food.order.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

@Component
public class ApprovalOutboxDataAccessMapper {

    public ApprovalOutboxEntity orderApprovalOutboxMessageToApprovalOutboxEntityy(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return ApprovalOutboxEntity.builder()
                .id(orderApprovalOutboxMessage.getId())
                .sagaId(orderApprovalOutboxMessage.getSagaId())
                .createdAt(orderApprovalOutboxMessage.getCreatedAt())
                .type(orderApprovalOutboxMessage.getType())
                .payload(orderApprovalOutboxMessage.getPayload())
                .order_status(orderApprovalOutboxMessage.getOrderStatus())
                .saga_status(orderApprovalOutboxMessage.getSagaStatus())
                .outbox_status(orderApprovalOutboxMessage.getOutboxStatus())
                .version(orderApprovalOutboxMessage.getVersion())
                .build();
    }

    public OrderApprovalOutboxMessage approvalOutboxEntityToOrderApprovalOutboxMessage(ApprovalOutboxEntity approvalOutboxEntity) {
        return OrderApprovalOutboxMessage.builder()
                .id(approvalOutboxEntity.getId())
                .sagaId(approvalOutboxEntity.getSagaId())
                .createdAt(approvalOutboxEntity.getCreatedAt())
                .type(approvalOutboxEntity.getType())
                .payload(approvalOutboxEntity.getPayload())
                .orderStatus(approvalOutboxEntity.getOrder_status())
                .sagaStatus(approvalOutboxEntity.getSaga_status())
                .outboxStatus(approvalOutboxEntity.getOutbox_status())
                .version(approvalOutboxEntity.getVersion())
                .build();
    }
}
