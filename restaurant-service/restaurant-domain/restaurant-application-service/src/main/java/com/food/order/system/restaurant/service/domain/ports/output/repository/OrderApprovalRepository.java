package com.food.order.system.restaurant.service.domain.ports.output.repository;

import com.food.order.system.restaurant.service.domain.entity.OrderApproval;

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
