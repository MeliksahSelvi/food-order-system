package com.food.order.system.order.service.dataaccess.restaurant.adapter;

import com.food.order.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.order.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.order.system.order.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.order.system.order.service.entity.Product;
import com.food.order.system.order.service.entity.Restaurant;
import com.food.order.system.order.service.ports.output.repository.RestaurantRepository;
import com.food.order.system.order.service.valueobject.Money;
import com.food.order.system.order.service.valueobject.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

/*todo check restaurant entity jpa entity why materialized view
 * Restaurant aggregate root'unun secondary adapter'ı
 * */
@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> productIds = getProductIds(restaurant);

        Optional<List<RestaurantEntity>> restaurantEntitiesOptional = restaurantJpaRepository.
                findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), productIds);

        checkRestaurantExist(restaurantEntitiesOptional);

        return restaurantEntitiesOptional.map(restaurantEntities ->
                restaurantEntities.stream().findFirst().get().toModel(getProducts(restaurantEntities))
        );
    }


    private List<UUID> getProductIds(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    private void checkRestaurantExist(Optional<List<RestaurantEntity>> restaurantEntitiesOptional) {
        restaurantEntitiesOptional.get().stream().findFirst().orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!"));
    }

    private List<Product> getProducts(List<RestaurantEntity> restaurantEntities) {
        List<Product> restaurantProducts = restaurantEntities.stream().map(restaurantEntity ->
                new Product(new ProductId(restaurantEntity.getProductId()),
                        restaurantEntity.getProductName(),
                        new Money(restaurantEntity.getProductPrice())
                )).toList();
        return restaurantProducts;
    }
}
