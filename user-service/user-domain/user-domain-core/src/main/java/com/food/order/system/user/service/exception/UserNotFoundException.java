package com.food.order.system.user.service.exception;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
