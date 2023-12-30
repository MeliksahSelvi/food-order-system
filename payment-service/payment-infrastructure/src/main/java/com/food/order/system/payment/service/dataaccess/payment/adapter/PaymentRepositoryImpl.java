package com.food.order.system.payment.service.dataaccess.payment.adapter;

import com.food.order.system.payment.service.dataaccess.payment.entity.PaymentEntity;
import com.food.order.system.payment.service.dataaccess.payment.repository.PaymentJpaRepository;
import com.food.order.system.payment.service.entity.Payment;
import com.food.order.system.payment.service.ports.output.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Payment aggregate root'unun secondary adapter'ı
 * */
@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .id(payment.getId().getValue())
                .customerId(payment.getCustomerId().getValue())
                .paymentstatus(payment.getPaymentstatus())
                .createdAt(payment.getCreatedAt())
                .orderId(payment.getOrderId().getValue())
                .price(payment.getPrice().getAmount())
                .build();
        return paymentJpaRepository.save(paymentEntity).toModel();
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentJpaRepository.findByOrderId(orderId).map(PaymentEntity::toModel);
    }
}
