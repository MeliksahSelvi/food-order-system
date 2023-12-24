package com.food.order.system.order.service.dataaccess.customer.mapper;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.food.order.system.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity){
        return Customer.builder()
                .customerId(new CustomerId(customerEntity.getId()))
                .build();
    }

    public CustomerEntity customerToCustomerEntity(Customer customer){
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
