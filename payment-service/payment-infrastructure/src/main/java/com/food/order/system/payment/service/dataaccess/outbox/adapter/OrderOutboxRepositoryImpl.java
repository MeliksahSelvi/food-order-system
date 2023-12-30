package com.food.order.system.payment.service.dataaccess.outbox.adapter;

import com.food.order.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.food.order.system.payment.service.dataaccess.outbox.exception.OrderOutboxNotFoundException;
import com.food.order.system.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import com.food.order.system.payment.service.outbox.common.OutboxStatus;
import com.food.order.system.payment.service.outbox.model.OrderOutboxMessage;
import com.food.order.system.payment.service.ports.output.repository.OrderOutboxRepository;
import com.food.order.system.payment.service.valueobject.PaymentStatus;
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

    @Override
    public OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxEntity orderOutboxEntity = OrderOutboxEntity.builder()
                .id(orderOutboxMessage.getId())
                .sagaId(orderOutboxMessage.getSagaId())
                .createdAt(orderOutboxMessage.getCreatedAt())
                .type(orderOutboxMessage.getType())
                .payload(orderOutboxMessage.getPayload())
                .outboxStatus(orderOutboxMessage.getOutboxStatus())
                .paymentStatus(orderOutboxMessage.getPaymentStatus())
                .version(orderOutboxMessage.getVersion())
                .build();
        return orderOutboxJpaRepository.save(orderOutboxEntity).toModel();
    }

    @Override
    public Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        return Optional.of(orderOutboxJpaRepository.findByTypeAndOutboxStatus(type, outboxStatus)
                .orElseThrow(() -> new OrderOutboxNotFoundException("Payment outbox object " +
                        "cannot be found for saga type " + type))
                .stream()
                .map(OrderOutboxEntity::toModel).collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type, UUID sagaId,
                                                                                           PaymentStatus paymentStatus, OutboxStatus outboxStatus) {

        return orderOutboxJpaRepository.
                findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(type, sagaId, paymentStatus, outboxStatus).map(OrderOutboxEntity::toModel);
    }

    @Override
    public void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {

        orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(type, outboxStatus);
    }
}
