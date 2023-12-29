package com.food.order.system.customer.service.ports.input.service;

import com.food.order.system.customer.service.dto.CreateCustomerCommand;
import com.food.order.system.customer.service.dto.CreateCustomerResponse;
import jakarta.validation.Valid;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

public interface CustomerApplicationService {

    CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);
}
