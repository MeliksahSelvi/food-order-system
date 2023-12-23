package com.food.order.system.restaurant.service.dataaccess.outbox.exception;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

public class OrderOutboxNotFoundException extends RuntimeException{
    public OrderOutboxNotFoundException(String message) {
        super(message);
    }
}
