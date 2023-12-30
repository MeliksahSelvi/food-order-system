package com.food.order.system.order.service.outbox.scheduler.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.order.service.common.DomainConstants;
import com.food.order.system.order.service.exception.OrderDomainException;
import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.payment.OrderPaymentEventPayload;
import com.food.order.system.order.service.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.order.service.ports.output.repository.PaymentOutBoxRepository;
import com.food.order.system.order.service.saga.SagaStatus;
import com.food.order.system.order.service.valueobject.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxHelper {

    private final PaymentOutBoxRepository paymentOutBoxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<List<OrderPaymentOutboxMessage>> getPaymentOutboxMessageByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {

        return paymentOutBoxRepository.findByTypeAndOutboxStatusAndSagaStatuses(DomainConstants.ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

    @Transactional(readOnly = true)
    public Optional<OrderPaymentOutboxMessage> getOutboxMessageBySagaIdAndSagaStatuses(
            UUID sagaId, SagaStatus... sagaStatuses) {

        return paymentOutBoxRepository.findByTypeAndSagaIdAndSagaStatuses(DomainConstants.ORDER_SAGA_NAME, sagaId, sagaStatuses);
    }

    @Transactional
    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        OrderPaymentOutboxMessage response = paymentOutBoxRepository.save(orderPaymentOutboxMessage);
        /*
         * Eğer saga stepinin güncel durumunu save ederken bir sorun oluşursa hemen durumu handle ediyoruz.
         * */
        if (response == null) {
            log.error("Could not save OrderPaymentOutboxMessage with outbox id: {}", orderPaymentOutboxMessage.getId());
            throw new OrderDomainException("Could not save OrderPaymentOutboxMessage with outbox id: " +
                    orderPaymentOutboxMessage.getId());
        }
        log.info("OrderPaymentOutboxMessage saved with outbox id: {}", orderPaymentOutboxMessage.getId());
    }

    @Transactional
    public void savePaymentOutboxMessage(OrderPaymentEventPayload paymentEventPayload, OrderStatus orderStatus,
                                         SagaStatus sagaStatus, OutboxStatus outboxStatus, UUID sagaId) {

        save(OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(paymentEventPayload.getCreatedAt())
                .type(DomainConstants.ORDER_SAGA_NAME)
                .payload(createPayload(paymentEventPayload))
                .orderStatus(orderStatus)
                .outboxStatus(outboxStatus)
                .sagaStatus(sagaStatus)
                .build());
    }

    @Transactional
    public void deletePaymentOutboxMessageByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {

        paymentOutBoxRepository.deleteByTypeAndOutboxStatusAndSagaStatuses(DomainConstants.ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

    @Transactional
    public void updateOutboxStatus(OrderPaymentOutboxMessage orderPaymentOutboxMessage, OutboxStatus outboxStatus) {
        orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
        save(orderPaymentOutboxMessage);
        log.info("OrderPaymentOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }

    private String createPayload(OrderPaymentEventPayload paymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(paymentEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderPaymentEventPayload object for order id: {}", paymentEventPayload.getOrderId(), e);
            throw new OrderDomainException("Could not create OrderPaymentEventPayload object for order id: " +
                    paymentEventPayload.getOrderId(), e);
        }
    }
}
