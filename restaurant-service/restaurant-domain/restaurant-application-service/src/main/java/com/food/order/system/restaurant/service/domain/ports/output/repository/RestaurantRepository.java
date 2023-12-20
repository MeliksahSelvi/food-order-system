package com.food.order.system.restaurant.service.domain.ports.output.repository;

import com.food.order.system.restaurant.service.domain.entity.Restaurant;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */
/*
 * Restaurant aggregate root'unun output portu
 * */
public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
