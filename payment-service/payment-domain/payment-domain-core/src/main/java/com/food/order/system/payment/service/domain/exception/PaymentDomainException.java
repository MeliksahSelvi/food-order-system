package com.food.order.system.payment.service.domain.exception;

import com.food.order.system.domain.exception.DomainException;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public class PaymentDomainException extends DomainException {
    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
