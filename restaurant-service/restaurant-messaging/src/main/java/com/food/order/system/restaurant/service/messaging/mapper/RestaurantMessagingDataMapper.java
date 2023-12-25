package com.food.order.system.restaurant.service.messaging.mapper;

import com.food.order.system.domain.event.payload.OrderApprovalEventPayload;
import com.food.order.system.domain.valueobject.ProductId;
import com.food.order.system.domain.valueobject.RestaurantOrderStatus;
import com.food.order.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.order.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.order.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.service.domain.entity.Product;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import debezium.order.restaurant_approval_outbox.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
public class RestaurantMessagingDataMapper {

    public RestaurantApprovalRequest restaurantApprovalRequestAvroModelToRestaurantApproval(
            OrderApprovalEventPayload orderApprovalEventPayload,
            Value requestAvroModel) {

        return RestaurantApprovalRequest.builder()
                .id(requestAvroModel.getId())
                .sagaId(requestAvroModel.getSagaId())
                .restaurantId(orderApprovalEventPayload.getRestaurantId())
                .orderId(orderApprovalEventPayload.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .products(orderApprovalEventPayload.getProducts().stream()
                        .map(avroModel ->
                                Product.builder()
                                        .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(orderApprovalEventPayload.getPrice())
                .createdAt(Instant.parse(requestAvroModel.getCreatedAt()))
                .build();
    }

    public RestaurantApprovalResponseAvroModel orderEventPayloadToApprovalResponseAvroModel(String sagaId, OrderEventPayload payload) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(payload.getOrderId())
                .setRestaurantId(payload.getRestaurantId())
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(payload.getOrderApprovalStatus()))
                .setFailureMessages(payload.getFailureMessages())
                .build();
    }
}
