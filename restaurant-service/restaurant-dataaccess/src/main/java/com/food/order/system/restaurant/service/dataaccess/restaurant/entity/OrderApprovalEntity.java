package com.food.order.system.restaurant.service.dataaccess.restaurant.entity;

import com.food.order.system.domain.valueobject.OrderApprovalStatus;
import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.domain.valueobject.RestaurantId;
import com.food.order.system.restaurant.service.domain.entity.OrderApproval;
import com.food.order.system.restaurant.service.domain.valueobject.OrderApprovalId;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_approval", schema = "restaurant")
public class OrderApprovalEntity {

    @Id
    private UUID id;
    private UUID restaurantId;
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    private OrderApprovalStatus status;

    public OrderApproval toModel() {
        return OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(id))
                .restaurantId(new RestaurantId(restaurantId))
                .orderId(new OrderId(orderId))
                .approvalStatus(status)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderApprovalEntity that = (OrderApprovalEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
