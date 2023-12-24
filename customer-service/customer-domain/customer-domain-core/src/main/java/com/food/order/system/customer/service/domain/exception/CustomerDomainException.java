package com.food.order.system.customer.service.domain.exception;

import com.food.order.system.domain.exception.DomainException;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

public class CustomerDomainException extends DomainException {
    public CustomerDomainException(String message) {
        super(message);
    }

    public CustomerDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
