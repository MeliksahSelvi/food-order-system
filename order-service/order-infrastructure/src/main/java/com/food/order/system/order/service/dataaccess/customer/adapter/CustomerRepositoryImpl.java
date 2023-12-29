package com.food.order.system.order.service.dataaccess.customer.adapter;

import com.food.order.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.food.order.system.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.food.order.system.order.service.entity.Customer;
import com.food.order.system.order.service.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

/*
 * Customer aggregate root'unun secondary adapter'ı
 * */
@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        Optional<CustomerEntity> customerEntityOptional = customerJpaRepository.findById(customerId);
        return customerEntityOptional.map(CustomerEntity::toModel);
    }

    @Transactional
    @Override
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = CustomerEntity.builder()
                .id(customer.getId().getValue())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
        return customerJpaRepository.save(customerEntity).toModel();
    }
}
