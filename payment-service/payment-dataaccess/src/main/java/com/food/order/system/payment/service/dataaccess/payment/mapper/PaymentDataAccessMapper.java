package com.food.order.system.payment.service.dataaccess.payment.mapper;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.payment.service.dataaccess.payment.entity.PaymentEntity;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.valueobject.PaymentId;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
public class PaymentDataAccessMapper {

    public PaymentEntity paymentToPaymentEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .customerId(payment.getCustomerId().getValue())
                .paymentStatus(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt())
                .orderId(payment.getOrderId().getValue())
                .price(payment.getPrice().getAmount())
                .build();
    }

    public Payment paymentEntityToPayment(PaymentEntity paymentEntity) {
        return Payment.builder()
                .paymentId(new PaymentId(paymentEntity.getId()))
                .customerId(new CustomerId(paymentEntity.getCustomerId()))
                .orderId(new OrderId(paymentEntity.getOrderId()))
                .createdAt(paymentEntity.getCreatedAt())
                .price(new Money(paymentEntity.getPrice()))
                .build();
    }
}
