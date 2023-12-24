package com.food.order.system.customer.service.domain.ports.output.repository;

import com.food.order.system.customer.service.domain.entity.Customer;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * Customer aggregate root'unun output portu
 * */
public interface CustomerRepository {

    Customer createCustomer(Customer customer);
}
