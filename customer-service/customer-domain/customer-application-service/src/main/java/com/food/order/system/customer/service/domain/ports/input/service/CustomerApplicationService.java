package com.food.order.system.customer.service.domain.ports.input.service;

import com.food.order.system.customer.service.domain.create.CreateCustomerCommand;
import com.food.order.system.customer.service.domain.create.CreateCustomerResponse;
import jakarta.validation.Valid;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

public interface CustomerApplicationService {

    CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);
}
