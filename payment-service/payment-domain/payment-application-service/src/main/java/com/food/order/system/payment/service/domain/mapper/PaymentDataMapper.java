package com.food.order.system.payment.service.domain.mapper;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.payment.service.domain.dto.PaymentRequest;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.valueobject.PaymentId;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

@Component
public class PaymentDataMapper {

    public Payment paymentRequestToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();

    }
}
