package com.food.order.system.payment.service.domain.ports.output.repository;

import com.food.order.system.payment.service.domain.entity.Payment;

import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */
/*
* Payment aggregate root'unun output portu
* */
public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(UUID orderId);
}
