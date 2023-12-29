package com.food.order.system.order.service.domain.entity;

import com.food.order.system.order.service.domain.common.AggregateRoot;
import com.food.order.system.order.service.domain.valueobject.CustomerId;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class Customer extends AggregateRoot<CustomerId> {

    private final String username;
    private final String firstName;
    private final String lastName;

    private Customer(Builder builder) {
        setId(builder.customerId);
        username = builder.username;
        firstName = builder.firstName;
        lastName = builder.lastName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public static final class Builder {
        private CustomerId customerId;
        private String username;
        private String firstName;
        private String lastName;

        private Builder() {
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }

    }
}
