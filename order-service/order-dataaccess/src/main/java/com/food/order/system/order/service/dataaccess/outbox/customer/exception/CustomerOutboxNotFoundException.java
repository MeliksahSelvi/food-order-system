package com.food.order.system.order.service.dataaccess.outbox.customer.exception;

/**
 * @Author mselvi
 * @Created 28.12.2023
 */

public class CustomerOutboxNotFoundException extends RuntimeException{

    public CustomerOutboxNotFoundException(String message) {
        super(message);
    }
}
