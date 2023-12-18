package com.food.order.system.order.service.messaging.mapper;

import com.food.order.system.domain.valueobject.OrderApprovalStatus;
import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.kafka.order.avro.model.*;
import com.food.order.system.order.service.domain.dto.message.PaymentResponse;
import com.food.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.event.OrderCancelledEvent;
import com.food.order.system.order.service.domain.event.OrderCreatedEvent;
import com.food.order.system.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")//todo fix
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")//todo fix
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    //todo onun bu methodu duplicate setlemeler içeriyordu bakalım düzeltecek mi?
    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")//todo fix
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setProducts(order.getItems().stream()
                        .map(orderItem -> Product.newBuilder()
                                .setId(orderItem.getProduct().getId().getValue().toString())
                                .setQuantity(orderItem.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel){
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToRestaurantApprovalResponse(RestaurantApprovalResponseAvroModel avroModel){
        return RestaurantApprovalResponse.builder()
                .id(avroModel.getId())
                .sagaId(avroModel.getSagaId())
                .restaurantId(avroModel.getRestaurantId())
                .orderId(avroModel.getOrderId())
                .createdAt(avroModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(avroModel.getOrderApprovalStatus().name()))
                .failureMessages(avroModel.getFailureMessages())
                .build();
    }

}
