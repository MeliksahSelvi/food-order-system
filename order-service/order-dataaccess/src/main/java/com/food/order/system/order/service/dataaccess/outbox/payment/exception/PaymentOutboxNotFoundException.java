package com.food.order.system.order.service.dataaccess.outbox.payment.exception;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

public class PaymentOutboxNotFoundException extends RuntimeException{

    public PaymentOutboxNotFoundException(String message) {
        super(message);
    }
}
