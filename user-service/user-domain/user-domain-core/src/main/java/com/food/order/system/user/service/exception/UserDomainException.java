package com.food.order.system.user.service.exception;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

public class UserDomainException extends RuntimeException{

    public UserDomainException(String message) {
        super(message);
    }

    public UserDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
