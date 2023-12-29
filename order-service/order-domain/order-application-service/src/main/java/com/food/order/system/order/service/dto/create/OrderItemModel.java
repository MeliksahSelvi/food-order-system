package com.food.order.system.order.service.dto.create;

import com.food.order.system.order.service.entity.OrderItem;
import com.food.order.system.order.service.entity.Product;
import com.food.order.system.order.service.valueobject.Money;
import com.food.order.system.order.service.valueobject.ProductId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class OrderItemModel {
    @NotNull
    private final UUID productId;
    @NotNull
    private final Integer quantity;
    @NotNull
    private final BigDecimal price;
    @NotNull
    private final BigDecimal subTotal;

    public OrderItem toModel() {
        return OrderItem.builder()
                .product(new Product(new ProductId(productId)))
                .price(new Money(price))
                .quantity(quantity)
                .subTotal(new Money(subTotal))
                .build();
    }
}
