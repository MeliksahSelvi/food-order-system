package com.food.order.system.customer.service.dataaccess.customer.adapter;

import com.food.order.system.customer.service.dataaccess.customer.entity.CustomerEntity;
import com.food.order.system.customer.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.food.order.system.customer.service.domain.entity.Customer;
import com.food.order.system.customer.service.domain.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * Customer aggregate root'unun secondary adapterı
 * */
@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        CustomerEntity customerEntity = CustomerEntity.builder()
                .id(customer.getId().getValue())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();

        return customerJpaRepository.save(customerEntity).toModel();
    }
}
