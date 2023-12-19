package com.food.order.system.payment.service.domain.entity;

import com.food.order.system.domain.entity.BaseEntity;
import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.payment.service.domain.valueobject.CreditEntryId;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
* Aggregate root olabilirdi bu sefer rest api açmamız gerekirdi.biz değerleri init sql data olarak vereceğiz.
* */
public class CreditEntry extends BaseEntity<CreditEntryId> {

    private final CustomerId customerId;
    private Money totalCreditAmount;//todo initialize değeri nerede?

    public void addCreditAmount(Money amount){
        totalCreditAmount=totalCreditAmount.add(amount);
    }

    public void substractCreditAmount(Money amount){
        totalCreditAmount=totalCreditAmount.substract(amount);
    }


    private CreditEntry(Builder builder) {
        setId(builder.creditEntryId);
        this.customerId=builder.customerId;
        this.totalCreditAmount=builder.totalCreditAmount;
    }

    public static Builder builder(){
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public static final class Builder{
        private CreditEntryId creditEntryId;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {
        }

        public Builder creditEntryId(CreditEntryId val){
            this.creditEntryId=val;
            return this;
        }

        public Builder customerId(CustomerId val){
            this.customerId=val;
            return this;
        }

        public Builder totalCreditAmount(Money val){
            this.totalCreditAmount=val;
            return this;
        }

        public CreditEntry build(){
            return new CreditEntry(this);
        }

    }
}
