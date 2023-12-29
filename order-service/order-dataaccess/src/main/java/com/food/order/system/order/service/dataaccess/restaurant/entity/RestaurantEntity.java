package com.food.order.system.order.service.dataaccess.restaurant.entity;

import com.food.order.system.order.service.domain.entity.Product;
import com.food.order.system.order.service.domain.entity.Restaurant;
import com.food.order.system.order.service.domain.valueobject.RestaurantId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 28.12.2023
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RestaurantEntityId.class)//multiple column primary key'e sahip olduğunu gösterip mapleme işlemi yapıyoruz.
@Table(name = "order_restaurant_m_view", schema = "restaurant")
public class RestaurantEntity {

    @Id
    private UUID restaurantId;
    @Id
    private UUID productId;
    private String restaurantName;
    private String productName;
    private BigDecimal productPrice;
    private Boolean restaurantActive;
    private Boolean productAvailable;

    public Restaurant toModel(List<Product> restaurantProducts) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantId))
                .products(restaurantProducts)
                .active(restaurantActive)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return restaurantId.equals(that.restaurantId) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, productId);
    }
}
