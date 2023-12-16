package com.food.order.system.order.service.domain.exception;

import com.food.order.system.domain.exception.DomainException;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
