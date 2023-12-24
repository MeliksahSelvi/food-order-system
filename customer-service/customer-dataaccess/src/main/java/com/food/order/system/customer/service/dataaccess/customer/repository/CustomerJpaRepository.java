package com.food.order.system.customer.service.dataaccess.customer.repository;

import com.food.order.system.customer.service.dataaccess.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
}
