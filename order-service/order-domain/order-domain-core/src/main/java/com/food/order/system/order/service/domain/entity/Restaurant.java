package com.food.order.system.order.service.domain.entity;

import com.food.order.system.order.service.domain.common.AggregateRoot;
import com.food.order.system.order.service.domain.valueobject.RestaurantId;

import java.util.List;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class Restaurant extends AggregateRoot<RestaurantId> {

    private final List<Product> products;
    private boolean active;

    private Restaurant(Builder builder) {
        setId(builder.restaurantId);
        this.products = builder.products;
        this.active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean getActive() {
        return active;
    }


    public static final class Builder {
        private RestaurantId restaurantId;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
