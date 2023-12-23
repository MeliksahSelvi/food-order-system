package com.food.order.system.restaurant.service.dataaccess.outbox.adapter;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.restaurant.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.food.order.system.restaurant.service.dataaccess.outbox.exception.OrderOutboxNotFoundException;
import com.food.order.system.restaurant.service.dataaccess.outbox.mapper.OrderOutboxDataAccessMapper;
import com.food.order.system.restaurant.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.order.system.restaurant.service.domain.ports.output.repository.OrderOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * order_outbox tablosunun secondary adapter'ı
 * */
@Component
@RequiredArgsConstructor
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {

    private final OrderOutboxJpaRepository orderOutboxJpaRepository;
    private final OrderOutboxDataAccessMapper orderOutboxDataAccessMapper;

    @Override
    public OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxEntity orderOutboxEntity = orderOutboxDataAccessMapper.
                orderOutBoxMessageToOrderOutBoxEntity(orderOutboxMessage);
        orderOutboxEntity = orderOutboxJpaRepository.save(orderOutboxEntity);
        return orderOutboxDataAccessMapper.orderOutboxEntityToOrderOutboxMessage(orderOutboxEntity);
    }

    @Override
    public Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        return Optional.of(orderOutboxJpaRepository.findByTypeAndOutboxStatus(type, outboxStatus)
                .orElseThrow(() -> new OrderOutboxNotFoundException("Approval outbox object " +
                        "cannot be found for saga type " + type))
                .stream()
                .map(orderOutboxDataAccessMapper::orderOutboxEntityToOrderOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderOutboxMessage> findByTypeAndSagaIdAndOutboxStatus(String type,
                                                                           UUID sagaId,
                                                                           OutboxStatus outboxStatus) {
        Optional<OrderOutboxEntity> orderOutboxEntity = orderOutboxJpaRepository.
                findByTypeAndSagaIdAndOutboxStatus(type, sagaId, outboxStatus);

        return orderOutboxEntity.map(orderOutboxDataAccessMapper::orderOutboxEntityToOrderOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(type, outboxStatus);
    }
}
