package com.food.order.system.order.service.exception;


/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class OrderDomainException extends RuntimeException {
    public OrderDomainException(String message) {
        super(message);
    }

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
