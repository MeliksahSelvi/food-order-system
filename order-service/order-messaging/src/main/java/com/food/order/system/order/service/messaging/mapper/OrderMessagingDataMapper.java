package com.food.order.system.order.service.messaging.mapper;

import com.food.order.system.domain.event.payload.PaymentOrderEventPayload;
import com.food.order.system.domain.event.payload.RestaurantOrderEventPayload;
import com.food.order.system.domain.valueobject.OrderApprovalStatus;
import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.order.system.order.service.domain.dto.message.CustomerModel;
import com.food.order.system.order.service.domain.dto.message.PaymentResponse;
import com.food.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import debezium.payment.order_outbox.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.food.order.system.domain.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

@Component
public class OrderMessagingDataMapper {

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentOrderEventPayload paymentOrderEventPayload,
                                                                     Value paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentOrderEventPayload.getPaymentId())
                .customerId(paymentOrderEventPayload.getCustomerId())
                .orderId(paymentOrderEventPayload.getOrderId())
                .price(paymentOrderEventPayload.getPrice())
                .createdAt(Instant.parse(paymentResponseAvroModel.getCreatedAt()))
                .paymentStatus(PaymentStatus.valueOf(paymentOrderEventPayload.getPaymentStatus()))
                .failureMessages(paymentOrderEventPayload.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToRestaurantApprovalResponse(RestaurantOrderEventPayload restaurantOrderEventPayload,
                                                                                            debezium.restaurant.order_outbox.Value avroModel) {
        return RestaurantApprovalResponse.builder()
                .id(avroModel.getId())
                .sagaId(avroModel.getSagaId())
                .restaurantId(restaurantOrderEventPayload.getRestaurantId())
                .orderId(restaurantOrderEventPayload.getOrderId())
                .createdAt(Instant.parse(avroModel.getCreatedAt()))
                .orderApprovalStatus(OrderApprovalStatus.valueOf(restaurantOrderEventPayload.getOrderApprovalStatus()))
                .failureMessages(restaurantOrderEventPayload.getFailureMessages())
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
