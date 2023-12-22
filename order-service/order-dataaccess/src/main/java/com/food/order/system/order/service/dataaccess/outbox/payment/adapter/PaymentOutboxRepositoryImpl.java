package com.food.order.system.order.service.dataaccess.outbox.payment.adapter;

import com.food.order.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.order.system.order.service.dataaccess.outbox.payment.exception.PaymentOutboxNotFoundException;
import com.food.order.system.order.service.dataaccess.outbox.payment.mapper.PaymentOutboxDataAccessMapper;
import com.food.order.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.order.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.order.service.domain.ports.output.repository.PaymentOutBoxRepository;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
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
    private final PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper;

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        PaymentOutboxEntity paymentOutboxEntity = paymentOutboxDataAccessMapper.
                orderPaymentOutboxMessageToPaymentOutboxEntity(orderPaymentOutboxMessage);

        paymentOutboxEntity = paymentOutboxJpaRepository.save(paymentOutboxEntity);
        return paymentOutboxDataAccessMapper.paymentOutboxEntityToOrderPaymentOutboxMessage(paymentOutboxEntity);
    }

    @Override
    public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                                                              OutboxStatus outboxStatus,
                                                                                              SagaStatus... sagaStatuses) {

        return Optional.of(paymentOutboxJpaRepository.findByTypeAndOutbox_statusAndSaga_statusIn(type,
                        outboxStatus, Arrays.asList(sagaStatuses))
                .orElseThrow(() -> new PaymentOutboxNotFoundException("Payment outbox object " +
                        "could not be found for saga type " + type))
                .stream()
                .map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(String type,
                                                                                  UUID sagaId,
                                                                                  SagaStatus... sagaStatuses) {
        return paymentOutboxJpaRepository.
                findByTypeAndSagaIdaAndSaga_statusIn(type, sagaId, Arrays.asList(sagaStatuses))
                .map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                           OutboxStatus outboxStatus,
                                                           SagaStatus... sagaStatuses) {

        paymentOutboxJpaRepository.deleteByTypeAndOutbox_statusAndSaga_statusIn(type, outboxStatus, Arrays.asList(sagaStatuses));
    }
}