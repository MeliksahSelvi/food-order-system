package com.food.order.system.payment.service.exception;


/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public class PaymentDomainException extends RuntimeException {
    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
