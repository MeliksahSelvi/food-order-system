package com.food.order.system.restaurant.service.ports.output.repository;

import com.food.order.system.restaurant.service.entity.Restaurant;

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
