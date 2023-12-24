package com.food.order.system.customer.service.domain;

import com.food.order.system.customer.service.domain.create.CreateCustomerCommand;
import com.food.order.system.customer.service.domain.entity.Customer;
import com.food.order.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.order.system.customer.service.domain.exception.CustomerDomainException;
import com.food.order.system.customer.service.domain.mapper.CustomerDataMapper;
import com.food.order.system.customer.service.domain.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerCreateHelper {

    private final CustomerDataMapper customerDataMapper;
    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    @Transactional
    public CustomerCreatedEvent persistCustomer(CreateCustomerCommand createCustomerCommand) {
        Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);
        CustomerCreatedEvent customerCreatedEvent = customerDomainService.validateAndInitiate(customer);
        Customer savedCustomer = customerRepository.createCustomer(customer);
        if (savedCustomer == null) {
            log.error("Could not save customer with id: {}", createCustomerCommand.getCustomerId());
            throw new CustomerDomainException("Could not save customer with id: " +
                    createCustomerCommand.getCustomerId());
        }
        log.info("Returning CustomerCreatedEvent for customer id: {}", createCustomerCommand.getCustomerId());
        return customerCreatedEvent;
    }
}
