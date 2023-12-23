package com.food.order.system.restaurant.service.messaging.mapper;

import com.food.order.system.domain.valueobject.ProductId;
import com.food.order.system.domain.valueobject.RestaurantOrderStatus;
import com.food.order.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.order.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.order.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.order.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.service.domain.entity.Product;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
public class RestaurantMessagingDataMapper {

    public RestaurantApprovalRequest restaurantApprovalRequestAvroModelToRestaurantApproval(
            RestaurantApprovalRequestAvroModel requestAvroModel) {

        return RestaurantApprovalRequest.builder()
                .id(requestAvroModel.getId())
                .sagaId(requestAvroModel.getSagaId())
                .restaurantId(requestAvroModel.getRestaurantId())
                .orderId(requestAvroModel.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(requestAvroModel.getRestaurantOrderStatus().name()))
                .products(requestAvroModel.getProducts().stream()
                        .map(avroModel ->
                                Product.builder()
                                        .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(requestAvroModel.getPrice())
                .createdAt(requestAvroModel.getCreatedAt())
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
