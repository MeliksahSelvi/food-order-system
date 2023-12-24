package com.food.order.system.customer.service.domain;

import com.food.order.system.customer.service.domain.create.CreateCustomerCommand;
import com.food.order.system.customer.service.domain.create.CreateCustomerResponse;
import com.food.order.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.order.system.customer.service.domain.mapper.CustomerDataMapper;
import com.food.order.system.customer.service.domain.outbox.scheduler.CustomerOutboxHelper;
import com.food.order.system.outbox.OutboxStatus;
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
    private final CustomerDataMapper customerDataMapper;
    private final CustomerOutboxHelper customerOutboxHelper;

    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {

        CustomerCreatedEvent customerCreatedEvent = customerCreateHelper.persistCustomer(createCustomerCommand);
        log.info("Customer is created with id: {}", customerCreatedEvent.getCustomer().getId().getValue());

        customerOutboxHelper.persistCustomerOutboxMessage(customerDataMapper.
                        customerCreatedEventToCustomerEventPayload(customerCreatedEvent),
                OutboxStatus.STARTED,
                UUID.randomUUID());
        log.info("Returning CreateCustomerResponse with customer id: {}", customerCreatedEvent.getCustomer().getId().getValue());
        return customerDataMapper.customerToCreateCustomerResponse(customerCreatedEvent.getCustomer(),
                "Customer Saved Successfully!");
    }
}
