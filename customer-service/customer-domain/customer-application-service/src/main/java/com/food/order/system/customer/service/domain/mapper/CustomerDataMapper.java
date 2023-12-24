package com.food.order.system.customer.service.domain.mapper;

import com.food.order.system.customer.service.domain.create.CreateCustomerCommand;
import com.food.order.system.customer.service.domain.create.CreateCustomerResponse;
import com.food.order.system.customer.service.domain.entity.Customer;
import com.food.order.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.outbox.customer.model.CustomerEventPayload;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Component
public class CustomerDataMapper {

    public Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand) {
        return Customer.builder()
                .customerId(new CustomerId(createCustomerCommand.getCustomerId()))
                .username(createCustomerCommand.getUsername())
                .firstName(createCustomerCommand.getFirstName())
                .lastName(createCustomerCommand.getLastName())
                .build();
    }

    public CreateCustomerResponse customerToCreateCustomerResponse(Customer customer, String message) {
        return new CreateCustomerResponse(customer.getId().getValue(), message);
    }

    public CustomerEventPayload customerCreatedEventToCustomerEventPayload(CustomerCreatedEvent customerCreatedEvent) {
        return CustomerEventPayload.builder()
                .customerId(customerCreatedEvent.getCustomer().getId().getValue().toString())
                .username(customerCreatedEvent.getCustomer().getUsername())
                .firstName(customerCreatedEvent.getCustomer().getFirstName())
                .lastName(customerCreatedEvent.getCustomer().getLastName())
                .createdAt(customerCreatedEvent.getCreatedAt())
                .build();
    }
}
