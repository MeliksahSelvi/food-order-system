package com.food.order.system.restaurant.service.domain;

import com.food.order.system.restaurant.service.domain.entity.Restaurant;
import com.food.order.system.restaurant.service.domain.event.OrderApprovalEvent;

import java.util.List;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

public interface RestaurantDomainService {

    OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages);
}
