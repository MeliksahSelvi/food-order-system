package com.food.order.system.payment.service.dataaccess.payment.adapter;

import com.food.order.system.payment.service.dataaccess.payment.entity.PaymentEntity;
import com.food.order.system.payment.service.dataaccess.payment.mapper.PaymentDataAccessMapper;
import com.food.order.system.payment.service.dataaccess.payment.repository.PaymentJpaRepository;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.ports.output.repository.PaymentRepository;
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
    private final PaymentDataAccessMapper paymentDataAccessMapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity paymentEntity = paymentDataAccessMapper.paymentToPaymentEntity(payment);
        paymentEntity = paymentJpaRepository.save(paymentEntity);
        return paymentDataAccessMapper.paymentEntityToPayment(paymentEntity);
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        Optional<PaymentEntity> paymentEntityOptional = paymentJpaRepository.findByOrderId(orderId);
        return paymentEntityOptional.map(paymentDataAccessMapper::paymentEntityToPayment);
    }
}
