package com.food.order.system.payment.service.domain.exception;


/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
