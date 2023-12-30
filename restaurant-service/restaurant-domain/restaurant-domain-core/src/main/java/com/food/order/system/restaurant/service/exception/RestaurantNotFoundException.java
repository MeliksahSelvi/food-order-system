package com.food.order.system.restaurant.service.exception;


/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
