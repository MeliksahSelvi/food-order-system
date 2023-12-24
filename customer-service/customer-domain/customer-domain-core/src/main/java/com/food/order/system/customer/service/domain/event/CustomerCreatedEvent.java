package com.food.order.system.customer.service.domain.event;

import com.food.order.system.customer.service.domain.entity.Customer;
import com.food.order.system.domain.event.DomainEvent;

import java.time.ZonedDateTime;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

public class CustomerCreatedEvent implements DomainEvent<Customer> {

    private final Customer customer;
    private final ZonedDateTime createdAt;

    public CustomerCreatedEvent(Customer customer, ZonedDateTime createdAt) {
        this.customer = customer;
        this.createdAt = createdAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
