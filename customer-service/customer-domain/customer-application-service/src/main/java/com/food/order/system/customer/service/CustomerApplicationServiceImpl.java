package com.food.order.system.customer.service;

import com.food.order.system.customer.service.dto.CreateCustomerCommand;
import com.food.order.system.customer.service.dto.CreateCustomerResponse;
import com.food.order.system.customer.service.ports.input.service.CustomerApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Slf4j
@Validated
@Component
@RequiredArgsConstructor
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final CustomerCreateCommandHandler customerCreateCommandHandler;

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
        return customerCreateCommandHandler.createCustomer(createCustomerCommand);
    }
}
