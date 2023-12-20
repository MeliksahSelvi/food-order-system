package com.food.order.system.payment.service.domain.entity;

import com.food.order.system.domain.entity.AggregateRoot;
import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.payment.service.domain.valueobject.PaymentId;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
* Payment Aggregate'inin Aggregate Root'u olarak bu Entity'i seçtik.
* */
public class Payment extends AggregateRoot<PaymentId> {

    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money price;

    private PaymentStatus paymentStatus;
    private ZonedDateTime createdAt;

    public void initializePayment() {
        setId(new PaymentId(UUID.randomUUID()));
        createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public void validatePayment(List<String> failureMessages) {
        if (price == null || !price.isGreaterThanZero()) {
            failureMessages.add("Total price must be greater than zero!");
        }
    }

    public void updateStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    private Payment(Builder builder) {
        setId(builder.paymentId);
        this.orderId = builder.orderId;
        this.customerId = builder.customerId;
        this.price = builder.price;
        this.paymentStatus = builder.paymentStatus;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getPrice() {
        return price;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public static final class Builder {
        private PaymentId paymentId;
        private OrderId orderId;
        private CustomerId customerId;
        private Money price;
        private PaymentStatus paymentStatus;
        private ZonedDateTime createdAt;

        private Builder() {
        }

        public Builder paymentId(PaymentId val) {
            this.paymentId = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            this.orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            this.customerId = val;
            return this;
        }

        public Builder price(Money val) {
            this.price = val;
            return this;
        }

        public Builder paymentStatus(PaymentStatus val) {
            this.paymentStatus = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            this.createdAt = val;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}
