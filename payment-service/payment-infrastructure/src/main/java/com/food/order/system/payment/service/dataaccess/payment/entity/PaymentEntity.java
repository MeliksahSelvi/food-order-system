package com.food.order.system.payment.service.dataaccess.payment.entity;

import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.valueobject.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
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
@Table(name = "payments")
public class PaymentEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private UUID orderId;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentstatus;
    private ZonedDateTime createdAt;

    public Payment toModel() {
        return Payment.builder()
                .paymentId(new PaymentId(id))
                .customerId(new CustomerId(customerId))
                .orderId(new OrderId(orderId))
                .createdAt(createdAt)
                .price(new Money(price))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
