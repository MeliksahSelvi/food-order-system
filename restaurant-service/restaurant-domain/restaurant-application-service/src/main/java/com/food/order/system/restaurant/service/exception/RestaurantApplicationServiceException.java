package com.food.order.system.restaurant.service.exception;


/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public class RestaurantApplicationServiceException extends RuntimeException {
    public RestaurantApplicationServiceException(String message) {
        super(message);
    }

    public RestaurantApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
