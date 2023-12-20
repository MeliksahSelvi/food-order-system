package com.food.order.system.payment.service.dataaccess.payment.exception;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public class PaymentDataAccessException extends RuntimeException{

    public PaymentDataAccessException(String message) {
        super(message);
    }
}
