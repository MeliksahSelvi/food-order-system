package com.food.order.system.restaurant.service.dataaccess.restaurant.adapter;

import com.food.order.system.restaurant.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.order.system.restaurant.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.order.system.restaurant.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.order.system.restaurant.service.domain.entity.Product;
import com.food.order.system.restaurant.service.domain.entity.Restaurant;
import com.food.order.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import com.food.order.system.restaurant.service.domain.valueobject.Money;
import com.food.order.system.restaurant.service.domain.valueobject.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Restaurant aggregate root'unun secondary adapter'ı
 * */
@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts = getProductIds(restaurant);

        Optional<List<RestaurantEntity>> restaurantEntitiesOptional = restaurantJpaRepository.
                findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProducts);

        checkRestaurantExist(restaurantEntitiesOptional);

        return restaurantEntitiesOptional.map(restaurantEntities ->
                restaurantEntities.stream().findFirst().get().toModel(getProducts(restaurantEntities)));
    }

    private List<UUID> getProductIds(Restaurant restaurant) {
        return restaurant.getOrderDetail().getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    private void checkRestaurantExist(Optional<List<RestaurantEntity>> restaurantEntitiesOptional) {
        restaurantEntitiesOptional.get().stream().findFirst().orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!"));
    }

    private List<Product> getProducts(List<RestaurantEntity> restaurantEntities) {
        return restaurantEntities.stream()
                .map(restaurantEntity ->
                        Product.builder()
                                .productId(new ProductId(restaurantEntity.getProductId()))
                                .name(restaurantEntity.getProductName())
                                .price(new Money(restaurantEntity.getProductPrice()))
                                .available(restaurantEntity.getProductAvailable())
                                .build()).collect(Collectors.toList());

    }
}
