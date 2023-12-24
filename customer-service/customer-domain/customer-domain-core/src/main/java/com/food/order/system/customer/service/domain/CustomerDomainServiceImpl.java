package com.food.order.system.customer.service.domain;

import com.food.order.system.customer.service.domain.entity.Customer;
import com.food.order.system.customer.service.domain.event.CustomerCreatedEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.food.order.system.domain.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Slf4j
public class CustomerDomainServiceImpl implements CustomerDomainService {
    @Override
    public CustomerCreatedEvent validateAndInitiate(Customer customer) {
        //todo add initialize and validate business logic methods
        log.info("Customer with id: {} is initiated", customer.getId().getValue());
        return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of(UTC)));
    }
}
