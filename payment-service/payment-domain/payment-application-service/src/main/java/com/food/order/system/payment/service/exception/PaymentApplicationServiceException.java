package com.food.order.system.payment.service.exception;


/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public class PaymentApplicationServiceException extends RuntimeException {
    public PaymentApplicationServiceException(String message) {
        super(message);
    }

    public PaymentApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
