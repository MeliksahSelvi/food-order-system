package com.food.order.system.restaurant.service.exception;


/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public class RestaurantDomainException extends RuntimeException {
    public RestaurantDomainException(String message) {
        super(message);
    }

    public RestaurantDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
