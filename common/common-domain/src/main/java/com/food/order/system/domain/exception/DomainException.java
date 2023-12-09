package com.food.order.system.domain.exception;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class DomainException extends RuntimeException{

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
