package com.food.order.system.payment.service.dataaccess.outbox.adapter;

import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.food.order.system.payment.service.dataaccess.outbox.exception.OrderOutboxNotFoundException;
import com.food.order.system.payment.service.dataaccess.outbox.mapper.OrderOutboxDataMapper;
import com.food.order.system.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import com.food.order.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.order.system.payment.service.domain.ports.output.repository.OrderOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 23.12.2023
 */

/*
 * order outbox tablosunun secondary adapterı
 * */
@Component
@RequiredArgsConstructor
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {

    private final OrderOutboxJpaRepository orderOutboxJpaRepository;
    private final OrderOutboxDataMapper orderOutboxDataMapper;

    @Override
    public OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxEntity orderOutboxEntity = orderOutboxDataMapper.orderOutboxMessageToOrderOutboxEntity(orderOutboxMessage);
        orderOutboxEntity = orderOutboxJpaRepository.save(orderOutboxEntity);
        return orderOutboxDataMapper.orderOutboxEntityToOrderOutboxMessage(orderOutboxEntity);
    }

    @Override
    public Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        return Optional.of(orderOutboxJpaRepository.findByTypeAndOutboxStatus(type, outboxStatus)
                .orElseThrow(() -> new OrderOutboxNotFoundException("Payment outbox object " +
                        "cannot be found for saga type " + type))
                .stream()
                .map(orderOutboxDataMapper::orderOutboxEntityToOrderOutboxMessage).collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type, UUID sagaId,
                                                                                           PaymentStatus paymentStatus, OutboxStatus outboxStatus) {

        Optional<OrderOutboxEntity> orderOutboxEntityOptional = orderOutboxJpaRepository.
                findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(type, sagaId, paymentStatus, outboxStatus);
        return orderOutboxEntityOptional.map(orderOutboxDataMapper::orderOutboxEntityToOrderOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {

        orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(type, outboxStatus);
    }
}
