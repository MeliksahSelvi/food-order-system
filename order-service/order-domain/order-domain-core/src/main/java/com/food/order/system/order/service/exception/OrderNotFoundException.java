package com.food.order.system.order.service.exception;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
