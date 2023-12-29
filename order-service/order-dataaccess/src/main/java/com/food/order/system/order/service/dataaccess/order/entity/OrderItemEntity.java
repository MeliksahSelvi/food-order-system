package com.food.order.system.order.service.dataaccess.order.entity;

import com.food.order.system.order.service.domain.entity.OrderItem;
import com.food.order.system.order.service.domain.entity.Product;
import com.food.order.system.order.service.domain.valueobject.Money;
import com.food.order.system.order.service.domain.valueobject.OrderItemId;
import com.food.order.system.order.service.domain.valueobject.ProductId;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

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
@IdClass(OrderItemEntityId.class)//multiple column primary key'e sahip olduğunu gösterip mapleme işlemi yapıyoruz.
@Table(name = "order_items")
/*
 * OrderItemEntity 2 tane primary key barındırıyor.
 * */
public class OrderItemEntity {
    @Id
    private Long id;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")//OrderEntity tablosunda bu name'ye sahip bir foreign key oluşacak
    private OrderEntity order;

    private UUID productId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;

    public OrderItem toModel() {
        return OrderItem.builder()
                .orderItemId(new OrderItemId(id))
                .price(new Money(price))
                .quantity(quantity)
                .subTotal(new Money(subTotal))
                .product(new Product(new ProductId(productId)))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return id.equals(that.id) && order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }
}
