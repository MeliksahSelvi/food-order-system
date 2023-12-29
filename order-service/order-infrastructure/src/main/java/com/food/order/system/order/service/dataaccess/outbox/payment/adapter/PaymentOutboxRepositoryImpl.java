package com.food.order.system.order.service.dataaccess.outbox.payment.adapter;

import com.food.order.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.order.system.order.service.dataaccess.outbox.payment.exception.PaymentOutboxNotFoundException;
import com.food.order.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.order.service.ports.output.repository.PaymentOutBoxRepository;
import com.food.order.system.order.service.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

/*
 * bu secondary adapter payment outbox entity'sinin output portunu implement ediyor.
 * */
@Component
@RequiredArgsConstructor
public class PaymentOutboxRepositoryImpl implements PaymentOutBoxRepository {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        PaymentOutboxEntity paymentOutboxEntity = PaymentOutboxEntity.builder()
                .id(orderPaymentOutboxMessage.getId())
                .sagaId(orderPaymentOutboxMessage.getSagaId())
                .createdAt(orderPaymentOutboxMessage.getCreatedAt())
                .type(orderPaymentOutboxMessage.getType())
                .payload(orderPaymentOutboxMessage.getPayload())
                .orderStatus(orderPaymentOutboxMessage.getOrderStatus())
                .sagaStatus(orderPaymentOutboxMessage.getSagaStatus())
                .outboxStatus(orderPaymentOutboxMessage.getOutboxStatus())
                .version(orderPaymentOutboxMessage.getVersion())
                .build();

        return paymentOutboxJpaRepository.save(paymentOutboxEntity).toModel();
    }

    @Override
    public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                                                              OutboxStatus outboxStatus,
                                                                                              SagaStatus... sagaStatuses) {

        return Optional.of(paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type,
                        outboxStatus, Arrays.asList(sagaStatuses))
                .orElseThrow(() -> new PaymentOutboxNotFoundException("Payment outbox object " +
                        "could not be found for saga type " + type))
                .stream()
                .map(PaymentOutboxEntity::toModel)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(String type,
                                                                                  UUID sagaId,
                                                                                  SagaStatus... sagaStatuses) {
        return paymentOutboxJpaRepository.
                findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatuses))
                .map(PaymentOutboxEntity::toModel);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                           OutboxStatus outboxStatus,
                                                           SagaStatus... sagaStatuses) {

        paymentOutboxJpaRepository.deleteBytypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.asList(sagaStatuses));
    }
}
