package com.food.order.system.payment.service.domain.exception;

import com.food.order.system.domain.exception.DomainException;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public class PaymentApplicationServiceException extends DomainException {
    public PaymentApplicationServiceException(String message) {
        super(message);
    }

    public PaymentApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
