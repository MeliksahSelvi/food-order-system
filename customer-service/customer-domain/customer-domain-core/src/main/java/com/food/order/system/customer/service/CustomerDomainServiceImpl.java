package com.food.order.system.customer.service;

import com.food.order.system.customer.service.common.DomainConstants;
import com.food.order.system.customer.service.entity.Customer;
import com.food.order.system.customer.service.event.CustomerCreatedEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Slf4j
public class CustomerDomainServiceImpl implements CustomerDomainService {
    @Override
    public CustomerCreatedEvent validateAndInitiate(Customer customer) {
        log.info("Customer with id: {} is initiated", customer.getId().getValue());
        return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    }
}
