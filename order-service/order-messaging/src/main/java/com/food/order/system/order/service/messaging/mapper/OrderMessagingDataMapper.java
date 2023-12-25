package com.food.order.system.order.service.messaging.mapper;

import com.food.order.system.domain.event.payload.PaymentOrderEventPayload;
import com.food.order.system.domain.event.payload.RestaurantOrderEventPayload;
import com.food.order.system.domain.valueobject.OrderApprovalStatus;
import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.order.service.domain.dto.message.CustomerModel;
import com.food.order.system.order.service.domain.dto.message.PaymentResponse;
import com.food.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.order.system.outbox.customer.model.CustomerEventPayload;
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

    public CustomerModel customerAvroModelToCustomerModel(CustomerEventPayload customerEventPayload,
                                                          debezium.customer.customer_outbox.Value customerAvroModel) {
        return CustomerModel.builder()
                .id(customerEventPayload.getCustomerId())
                .username(customerEventPayload.getUsername())
                .firstName(customerEventPayload.getFirstName())
                .lastName(customerEventPayload.getLastName())
                .createdAt(ZonedDateTime.ofInstant(Instant.parse(customerAvroModel.getCreatedAt()), ZoneId.of(UTC)))
                .build();
    }
}
