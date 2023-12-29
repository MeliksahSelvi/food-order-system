package com.food.order.system.order.service.dataaccess.order.entity;

import com.food.order.system.order.service.valueobject.StreetAddress;
import jakarta.persistence.*;
import lombok.*;

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
@Table(name = "order_address")
public class OrderAddressEntity {

    @Id
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")//OrderEntity tablosunda bu name'ye sahip bir foreign key oluşacak
    private OrderEntity order;

    private String street;
    private String postalCode;
    private String city;

    public StreetAddress toObject() {
        return new StreetAddress(id, street, postalCode, city);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderAddressEntity that = (OrderAddressEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
