package com.food.order.system.customer.service;

import com.food.order.system.customer.service.entity.Customer;
import com.food.order.system.customer.service.event.CustomerCreatedEvent;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

public interface CustomerDomainService {

    CustomerCreatedEvent validateAndInitiate(Customer customer);
}
