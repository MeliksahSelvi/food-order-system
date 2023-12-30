package com.food.order.system.user.service.dataaccess.user.repository;

import com.food.order.system.user.service.dataaccess.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);
}
