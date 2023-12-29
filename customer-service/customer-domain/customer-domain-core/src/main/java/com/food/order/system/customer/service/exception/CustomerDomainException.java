package com.food.order.system.customer.service.exception;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

public class CustomerDomainException extends RuntimeException {
    public CustomerDomainException(String message) {
        super(message);
    }

    public CustomerDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
