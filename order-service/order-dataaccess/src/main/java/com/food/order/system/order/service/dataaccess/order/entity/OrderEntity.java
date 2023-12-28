package com.food.order.system.order.service.dataaccess.order.entity;

import com.food.order.system.domain.valueobject.*;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.valueobject.TrackingId;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.food.order.system.order.service.domain.entity.Order.FAILURE_MESSAGES_DELIMITER;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private UUID restaurantId;
    private UUID trackingId;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String failureMessages;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderAddressEntity address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items;

    public Order toModel() {
        return Order.builder()
                .orderId(new OrderId(id))
                .restaurantId(new RestaurantId(restaurantId))
                .customerId(new CustomerId(customerId))
                .deliveryAddress(address.toObject())
                .price(new Money(price))
                .items(items.stream().map(OrderItemEntity::toModel).collect(Collectors.toList()))
                .trackingId(new TrackingId(trackingId))
                .orderStatus(orderStatus)
                .failureMessages(failureMessages.isEmpty() ? new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(failureMessages.split(FAILURE_MESSAGES_DELIMITER))))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
