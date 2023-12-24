package com.food.order.system.customer.service.dataaccess.customer.adapter;

import com.food.order.system.customer.service.dataaccess.customer.entity.CustomerEntity;
import com.food.order.system.customer.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
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
    private final CustomerDataAccessMapper customerDataAccessMapper;

    @Override
    public Customer createCustomer(Customer customer) {
        CustomerEntity customerEntity = customerDataAccessMapper.customerToCustomerEntity(customer);
        customerEntity = customerJpaRepository.save(customerEntity);
        return customerDataAccessMapper.customerEntityToCustomer(customerEntity);
    }
}
