package com.food.order.system.payment.service.domain.outbox.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.payment.service.domain.exception.PaymentDomainException;
import com.food.order.system.payment.service.domain.outbox.common.OutboxStatus;
import com.food.order.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.order.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.order.system.payment.service.domain.ports.output.repository.OrderOutboxRepository;
import com.food.order.system.payment.service.domain.valueobject.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.food.order.system.payment.service.domain.constants.DomainConstants.ORDER_SAGA_NAME;
import static com.food.order.system.payment.service.domain.constants.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 23.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxHelper {

    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        return orderOutboxRepository.findByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional(readOnly = true)
    public Optional<OrderOutboxMessage> getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(UUID sagaId,
                                                                                               PaymentStatus paymentStatus) {
        return orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(ORDER_SAGA_NAME, sagaId, paymentStatus, OutboxStatus.COMPLETED);
    }

    @Transactional
    public void save(OrderOutboxMessage orderOutboxMessage) {

        OrderOutboxMessage response = orderOutboxRepository.save(orderOutboxMessage);
        /*
         * Eğer saga stepinin güncel durumunu save ederken bir sorun oluşursa hemen durumu handle ediyoruz.
         * */
        if (response == null) {
            log.error("Could not save OrderOutboxMessage with outbox id: {}", orderOutboxMessage.getId());
            throw new PaymentDomainException("Could not save OrderOutboxMessage with outbox id: " +
                    orderOutboxMessage.getId());
        }
        log.info("OrderOutboxMessage saved with outbox id: {}", orderOutboxMessage.getId());
    }

    @Transactional
    public void persistOrderOutboxMessage(OrderEventPayload orderEventPayload,
                                          PaymentStatus paymentStatus,
                                          OutboxStatus outboxStatus,
                                          UUID sagaId) {

        save(OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .type(ORDER_SAGA_NAME)
                .createdAt(orderEventPayload.getCreatedAt())
                .processedAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .payload(createPayload(orderEventPayload))
                .paymentStatus(paymentStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    @Transactional
    public void deleteByOutboxStatus(OutboxStatus outboxStatus) {
        orderOutboxRepository.deleteByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void updateOutboxStatus(OrderOutboxMessage orderOutboxMessage, OutboxStatus outboxStatus) {
        orderOutboxMessage.setOutboxStatus(outboxStatus);
        save(orderOutboxMessage);
        log.info("OrderOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }

    private String createPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderEventPayload object for order id: {}", orderEventPayload.getOrderId(), e);
            throw new PaymentDomainException("Could not create orderEventPayload object for order id: " +
                    orderEventPayload.getOrderId(), e);
        }
    }
}
