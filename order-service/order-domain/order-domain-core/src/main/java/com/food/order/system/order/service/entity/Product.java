package com.food.order.system.order.service.entity;

import com.food.order.system.order.service.common.BaseEntity;
import com.food.order.system.order.service.valueobject.Money;
import com.food.order.system.order.service.valueobject.ProductId;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        setId(productId);
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId) {
        setId(productId);
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
