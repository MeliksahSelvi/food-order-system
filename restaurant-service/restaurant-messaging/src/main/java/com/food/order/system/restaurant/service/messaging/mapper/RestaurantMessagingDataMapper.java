package com.food.order.system.restaurant.service.messaging.mapper;

import com.food.order.system.domain.valueobject.ProductId;
import com.food.order.system.domain.valueobject.RestaurantOrderStatus;
import com.food.order.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.order.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.order.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.order.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.service.domain.entity.Product;
import com.food.order.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.order.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.order.system.restaurant.service.domain.event.OrderRejectedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
public class RestaurantMessagingDataMapper {

    public RestaurantApprovalResponseAvroModel orderApprovedEventToRestaurantApprovalResponseAvroModel(OrderApprovedEvent orderApprovedEvent) {
        return buildRestaurantApprovalResponseAvroModel(orderApprovedEvent);
    }

    public RestaurantApprovalResponseAvroModel orderRejectedEventToRestaurantApprovalResponseAvroModel(OrderRejectedEvent orderRejectedEvent) {
        return buildRestaurantApprovalResponseAvroModel(orderRejectedEvent);
    }

    public RestaurantApprovalRequest restaurantApprovalRequestAvroModelToRestaurantApproval(
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel) {

        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
                .orderId(restaurantApprovalRequestAvroModel.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel.getRestaurantOrderStatus().name()))
                .products(restaurantApprovalRequestAvroModel.getProducts().stream()
                        .map(avroModel ->
                                Product.builder()
                                        .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(restaurantApprovalRequestAvroModel.getPrice())
                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
                .build();
    }

    private RestaurantApprovalResponseAvroModel buildRestaurantApprovalResponseAvroModel(OrderApprovalEvent orderApprovalEvent) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(orderApprovalEvent.getOrderApproval().getOrderId().getValue().toString())
                .setRestaurantId(orderApprovalEvent.getRestaurantId().getValue().toString())
                .setCreatedAt(orderApprovalEvent.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApprovalEvent.getOrderApproval().getApprovalStatus().name()))
                .setFailureMessages(orderApprovalEvent.getFailureMessages())
                .build();
    }
}