package com.food.order.system.restaurant.service.ports.output.repository;

import com.food.order.system.restaurant.service.entity.OrderApproval;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
* OrderApproval entity'sinin output portu
* */
public interface OrderApprovalRepository {
    OrderApproval save(OrderApproval orderApproval);
}
