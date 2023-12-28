package com.food.order.system.order.service.domain;

import com.food.order.system.order.service.domain.dto.message.CustomerModel;
import com.food.order.system.order.service.domain.entity.Customer;
import com.food.order.system.order.service.domain.exception.OrderDomainException;
import com.food.order.system.order.service.domain.outbox.model.customer.CustomerEventPayload;
import com.food.order.system.order.service.domain.outbox.scheduler.customer.CustomerOutboxHelper;
import com.food.order.system.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import com.food.order.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.order.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * bu listener (primary adapter) customer  bounded context'i tarafından gönderilen eventi takip eden
 * ve customer'ı DB'ye persist eden primary adapter
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerMessageListenerImpl implements CustomerMessageListener {

    private final CustomerRepository customerRepository;
    private final CustomerOutboxHelper customerOutboxHelper;

    @Override
    public void customerCreated(CustomerModel customerModel) {
        Customer savedCustomer = customerRepository.save(customerModel.toModel());
        if (savedCustomer == null) {
            log.error("Customer could not be created in order database with id: {}", customerModel.getId());
            throw new OrderDomainException("Customer could not be created in order database with id: " +
                    customerModel.getId());
        }

        customerOutboxHelper.persistCustomerOutboxMessage(createCustomerEventPayload(customerModel), OutboxStatus.STARTED, UUID.randomUUID());
        log.info("Customer is created in order database with id: {}", customerModel.getId());
    }

    private CustomerEventPayload createCustomerEventPayload(CustomerModel model) {
        return CustomerEventPayload.builder()
                .customerId(model.getId())
                .username(model.getUsername())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .createdAt(model.getCreatedAt())
                .build();
    }
}
