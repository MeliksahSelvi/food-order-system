package com.food.order.system.customer.service;

import com.food.order.system.customer.service.dto.CreateCustomerCommand;
import com.food.order.system.customer.service.dto.CreateCustomerResponse;
import com.food.order.system.customer.service.event.CustomerCreatedEvent;
import com.food.order.system.customer.service.common.OutboxStatus;
import com.food.order.system.customer.service.outbox.model.CustomerEventPayload;
import com.food.order.system.customer.service.outbox.scheduler.CustomerOutboxHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerCreateCommandHandler {

    private final CustomerCreateHelper customerCreateHelper;
    private final CustomerOutboxHelper customerOutboxHelper;

    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {

        CustomerCreatedEvent customerCreatedEvent = customerCreateHelper.persistCustomer(createCustomerCommand);
        log.info("Customer is created with id: {}", customerCreatedEvent.getCustomer().getId().getValue());

        customerOutboxHelper.persistCustomerOutboxMessage(createCustomerEventPayload(customerCreatedEvent),
                OutboxStatus.STARTED,
                UUID.randomUUID());
        log.info("Returning CreateCustomerResponse with customer id: {}", customerCreatedEvent.getCustomer().getId().getValue());
        return new CreateCustomerResponse(customerCreatedEvent.getCustomer().getId().getValue(), "Customer Saved Successfully!");
    }

    private CustomerEventPayload createCustomerEventPayload(CustomerCreatedEvent customerCreatedEvent) {
        return CustomerEventPayload.builder()
                .customerId(customerCreatedEvent.getCustomer().getId().getValue().toString())
                .username(customerCreatedEvent.getCustomer().getUsername())
                .firstName(customerCreatedEvent.getCustomer().getFirstName())
                .lastName(customerCreatedEvent.getCustomer().getLastName())
                .createdAt(customerCreatedEvent.getCreatedAt())
                .build();
    }
}
