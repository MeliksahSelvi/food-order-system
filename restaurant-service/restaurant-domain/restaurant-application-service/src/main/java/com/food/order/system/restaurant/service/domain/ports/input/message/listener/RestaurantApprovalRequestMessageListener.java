package com.food.order.system.restaurant.service.domain.ports.input.message.listener;

import com.food.order.system.restaurant.service.domain.dto.RestaurantApprovalRequest;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
* bir order processing'de payment işlemi olduktan sonra  restaurant bounded context'inden onay almak için fırlatılan eventin
* dinleyen input portu.
* */
public interface RestaurantApprovalRequestMessageListener {

    void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);
}
