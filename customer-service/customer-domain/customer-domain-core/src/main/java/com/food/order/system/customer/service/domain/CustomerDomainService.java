package com.food.order.system.customer.service.domain;

import com.food.order.system.customer.service.domain.entity.Customer;
import com.food.order.system.customer.service.domain.event.CustomerCreatedEvent;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

public interface CustomerDomainService {

    CustomerCreatedEvent validateAndInitiate(Customer customer);
}
