package com.food.order.system.customer.service.dataaccess.outbox.adapter;

import com.food.order.system.customer.service.domain.ports.output.repository.CustomerOutboxRepository;
import com.food.order.system.dataaccess.customer.entity.CustomerOutboxEntity;
import com.food.order.system.dataaccess.customer.exception.CustomerOutboxNotFoundException;
import com.food.order.system.dataaccess.customer.mapper.CustomerOutboxDataAccessMapper;
import com.food.order.system.dataaccess.customer.repository.CustomerOutboxJpaRepository;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.outbox.customer.model.CustomerOutboxMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * customer_outbox tablosunun secondary adapterı
 * */
@Component
@RequiredArgsConstructor
public class CustomerOutboxRepositoryImpl implements CustomerOutboxRepository {

    private final CustomerOutboxJpaRepository customerOutboxJpaRepository;
    private final CustomerOutboxDataAccessMapper customerOutboxDataAccessMapper;

    @Override
    public CustomerOutboxMessage save(CustomerOutboxMessage orderOutboxMessage) {

        CustomerOutboxEntity customerOutboxEntity = customerOutboxDataAccessMapper.
                customerOutboxMessageToCustomerOutboxEntity(orderOutboxMessage);
        customerOutboxEntity = customerOutboxJpaRepository.save(customerOutboxEntity);
        return customerOutboxDataAccessMapper.customerOutboxEntityToCustomerOutboxMessage(customerOutboxEntity);
    }

    @Override
    public Optional<List<CustomerOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        return Optional.of(customerOutboxJpaRepository.findByTypeAndOutboxStatus(type, outboxStatus)
                .orElseThrow(() -> new CustomerOutboxNotFoundException("Customer outbox object " +
                        "cannot be found for saga type " + type))
                .stream()
                .map(customerOutboxDataAccessMapper::customerOutboxEntityToCustomerOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        customerOutboxJpaRepository.deleteByTypeAndOutboxStatus(type, outboxStatus);
    }
}
