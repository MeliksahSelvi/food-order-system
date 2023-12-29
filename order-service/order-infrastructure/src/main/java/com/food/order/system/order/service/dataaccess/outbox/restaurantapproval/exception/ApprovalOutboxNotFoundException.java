package com.food.order.system.order.service.dataaccess.outbox.restaurantapproval.exception;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

public class ApprovalOutboxNotFoundException extends RuntimeException{

    public ApprovalOutboxNotFoundException(String message) {
        super(message);
    }
}
