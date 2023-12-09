package com.food.order.system.order.service.domain.entity;

import com.food.order.system.domain.entity.BaseEntity;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.ProductId;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId,String name, Money price) {
        setId(productId);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
