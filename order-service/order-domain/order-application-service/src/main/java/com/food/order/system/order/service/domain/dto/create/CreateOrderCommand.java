package com.food.order.system.order.service.domain.dto.create;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.RestaurantId;
import com.food.order.system.order.service.domain.entity.Order;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
    private final List<OrderItemModel> items;
    @NotNull
    private final OrderAddress address;

    public Order toModel() {
        return Order.builder()
                .customerId(new CustomerId(customerId))
                .restaurantId(new RestaurantId(restaurantId))
                .price(new Money(price))
                .deliveryAddress(address.toObject())
                .items(items.stream().map(OrderItemModel::toModel).toList())
                .build();
    }
}
