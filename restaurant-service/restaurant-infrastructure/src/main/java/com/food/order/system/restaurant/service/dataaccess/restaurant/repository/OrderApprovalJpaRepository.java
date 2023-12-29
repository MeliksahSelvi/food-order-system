package com.food.order.system.restaurant.service.dataaccess.restaurant.repository;

import com.food.order.system.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Repository
public interface OrderApprovalJpaRepository extends JpaRepository<OrderApprovalEntity, UUID> {

}
