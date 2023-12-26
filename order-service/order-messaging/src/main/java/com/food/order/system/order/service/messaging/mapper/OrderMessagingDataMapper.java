package com.food.order.system.order.service.messaging.mapper;

import com.food.order.system.domain.valueobject.OrderApprovalStatus;
import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.kafka.order.avro.model.*;
import com.food.order.system.order.service.domain.dto.message.CustomerModel;
import com.food.order.system.order.service.domain.dto.message.PaymentResponse;
import com.food.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.order.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.order.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.food.order.system.domain.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

@Component
public class OrderMessagingDataMapper {

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
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

    public RestaurantApprovalResponse approvalResponseAvroModelToRestaurantApprovalResponse(RestaurantApprovalResponseAvroModel avroModel) {
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

    public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId,
                                                                              OrderPaymentEventPayload payload) {

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setCustomerId(payload.getCustomerId())
                .setOrderId(payload.getOrderId())
                .setPrice(payload.getPrice())
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(payload.getPaymentOrderStatus()))
                .build();
    }

    public RestaurantApprovalRequestAvroModel
    orderApprovalEventToRestaurantApprovalRequestAvroModel(String sagaId, OrderApprovalEventPayload payload) {

        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(payload.getOrderId())
                .setRestaurantId(payload.getRestaurantId())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(payload.getRestaurantOrderStatus()))
                .setProducts(payload.getProducts().stream()
                        .map(approvalProduct -> Product.newBuilder()
                                .setId(approvalProduct.getId())
                                .setQuantity(approvalProduct.getQuantity())
                                .build()).
                        collect(Collectors.toList()))
                .setPrice(payload.getPrice())
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .build();
    }

    public CustomerModel customerAvroModelToCustomerModel(CustomerAvroModel customerAvroModel) {
        return CustomerModel.builder()
                .id(customerAvroModel.getId())
                .username(customerAvroModel.getUsername())
                .firstName(customerAvroModel.getFirstName())
                .lastName(customerAvroModel.getLastName())
                .createdAt(ZonedDateTime.ofInstant(customerAvroModel.getCreatedAt(), ZoneId.of(UTC)))
                .build();
    }
}
