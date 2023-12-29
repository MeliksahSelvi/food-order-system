package com.food.order.system.payment.service.dataaccess.outbox.exception;

/**
 * @Author mselvi
 * @Created 23.12.2023
 */

public class OrderOutboxNotFoundException extends RuntimeException{
    public OrderOutboxNotFoundException(String message) {
        super(message);
    }
}
