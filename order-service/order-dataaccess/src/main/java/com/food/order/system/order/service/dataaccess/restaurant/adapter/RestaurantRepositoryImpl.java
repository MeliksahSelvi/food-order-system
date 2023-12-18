package com.food.order.system.order.service.dataaccess.restaurant.adapter;

import com.food.order.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.order.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.order.system.order.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.order.system.order.service.domain.entity.Restaurant;
import com.food.order.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;


    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> productIds = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntitiesOptional = restaurantJpaRepository.
                findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), productIds);
        return restaurantEntitiesOptional.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}