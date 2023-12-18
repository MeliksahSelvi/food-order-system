package com.food.order.system.order.service.dataaccess.customer.adapter;

import com.food.order.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.food.order.system.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.food.order.system.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.food.order.system.order.service.domain.entity.Customer;
import com.food.order.system.order.service.domain.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;


    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        Optional<CustomerEntity> customerEntityOptional = customerJpaRepository.findById(customerId);
        return customerEntityOptional.map(customerDataAccessMapper::customerEntityToCustomer);
    }
}
