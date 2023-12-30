package com.food.order.system.payment.service.dataaccess.credithistory.entity;

import com.food.order.system.payment.service.entity.CreditHistory;
import com.food.order.system.payment.service.valueobject.CreditHistoryId;
import com.food.order.system.payment.service.valueobject.CustomerId;
import com.food.order.system.payment.service.valueobject.Money;
import com.food.order.system.payment.service.valueobject.TransactionType;
import jakarta.persistence.*;
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
@Table(name = "credit_history")
public class CreditHistoryEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public CreditHistory toModel() {
        return CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(id))
                .customerId(new CustomerId(customerId))
                .amount(new Money(amount))
                .transactionType(transactionType)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditHistoryEntity that = (CreditHistoryEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
