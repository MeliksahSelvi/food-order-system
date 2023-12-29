package com.food.order.system.order.service.ports.output.repository;

import com.food.order.system.order.service.entity.Restaurant;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Restaurant Aggregate Root'unun output portu
 * */
public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
