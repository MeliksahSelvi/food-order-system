package com.food.order.system.order.service.domain.dto.create;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.ProductId;
import com.food.order.system.domain.valueobject.RestaurantId;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.entity.Product;
import com.food.order.system.order.service.domain.valueobject.StreetAddress;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderCommand {
    @NotNull
    private final UUID customerId;
    @NotNull
    private final UUID restaurantId;
    @NotNull
    private final BigDecimal price;
    @NotNull
    private final List<OrderItem> items;
    @NotNull
    private final OrderAddress address;

    public Order toModel() {
        return Order.builder()
                .customerId(new CustomerId(customerId))
                .restaurantId(new RestaurantId(restaurantId))
                .price(new Money(price))
                .deliveryAddress(address.toObject())
                .items(items.stream().map(OrderItem::toModel).toList())
                .build();
    }
}
