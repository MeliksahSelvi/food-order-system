package com.food.order.system.order.service.ports.output.repository;

import com.food.order.system.order.service.entity.Customer;

import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Customer Aggregate Root'unun output portu
 * */
public interface CustomerRepository {

    Optional<Customer> findCustomer(UUID customerId);

    Customer save(Customer customer);
}
