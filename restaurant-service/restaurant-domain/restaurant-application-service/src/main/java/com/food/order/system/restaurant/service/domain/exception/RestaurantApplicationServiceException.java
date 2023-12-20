package com.food.order.system.restaurant.service.domain.exception;

import com.food.order.system.domain.exception.DomainException;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public class RestaurantApplicationServiceException extends DomainException {
    public RestaurantApplicationServiceException(String message) {
        super(message);
    }

    public RestaurantApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
