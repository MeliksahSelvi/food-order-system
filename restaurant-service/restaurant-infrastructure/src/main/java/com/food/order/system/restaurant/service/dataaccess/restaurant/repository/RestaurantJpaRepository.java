package com.food.order.system.restaurant.service.dataaccess.restaurant.repository;

import com.food.order.system.restaurant.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.order.system.restaurant.service.dataaccess.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 28.12.2023
 */

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {

    //method name içindeki in ifadesi verilen list içindeki id'ler ile where clause içinde in ifadesini kullanması için
    Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);
}
