package com.food.order.system.dataaccess.customer.exception;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

public class CustomerOutboxNotFoundException extends RuntimeException{

    public CustomerOutboxNotFoundException(String message) {
        super(message);
    }
}
