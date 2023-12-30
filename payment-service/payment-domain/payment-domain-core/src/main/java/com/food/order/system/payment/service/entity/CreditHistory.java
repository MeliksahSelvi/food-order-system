package com.food.order.system.payment.service.entity;

import com.food.order.system.payment.service.common.BaseEntity;
import com.food.order.system.payment.service.valueobject.CreditHistoryId;
import com.food.order.system.payment.service.valueobject.CustomerId;
import com.food.order.system.payment.service.valueobject.Money;
import com.food.order.system.payment.service.valueobject.TransactionType;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
 * Aggregate root olabilirdi bu sefer rest api açmamız gerekirdi.biz değerleri init sql data olarak vereceğiz.
 * */
public class CreditHistory extends BaseEntity<CreditHistoryId> {

    private final CustomerId customerId;
    private final Money amount;
    private final TransactionType transactionType;

    private CreditHistory(Builder builder) {
        setId(builder.creditHistoryId);
        customerId = builder.customerId;
        amount = builder.amount;
        transactionType = builder.transactionType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public static final class Builder {
        private CreditHistoryId creditHistoryId;
        private CustomerId customerId;
        private Money amount;
        private TransactionType transactionType;

        private Builder() {

        }

        public Builder creditHistoryId(CreditHistoryId val) {
            creditHistoryId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder amount(Money val) {
            amount = val;
            return this;
        }

        public Builder transactionType(TransactionType val) {
            transactionType = val;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }
}
