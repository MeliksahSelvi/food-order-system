package com.food.order.system.payment.service.dataaccess.creditentry.entity;

import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.payment.service.domain.valueobject.CreditEntryId;
import com.food.order.system.payment.service.domain.valueobject.CustomerId;
import com.food.order.system.payment.service.domain.valueobject.Money;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
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
@Table(name = "credit_entry")
public class CreditEntryEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private BigDecimal totalCreditAmount;

    public CreditEntry toModel() {
        return CreditEntry.builder()
                .creditEntryId(new CreditEntryId(id))
                .customerId(new CustomerId(customerId))
                .totalCreditAmount(new Money(totalCreditAmount))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditEntryEntity that = (CreditEntryEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
