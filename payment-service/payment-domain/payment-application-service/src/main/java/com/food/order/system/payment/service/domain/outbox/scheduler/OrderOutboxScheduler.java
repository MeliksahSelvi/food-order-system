package com.food.order.system.payment.service.domain.outbox.scheduler;

import com.food.order.system.payment.service.domain.outbox.common.OutboxScheduler;
import com.food.order.system.payment.service.domain.outbox.common.OutboxStatus;
import com.food.order.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.order.system.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

/*
 * Burada order service ile iletişim kurması gereken outbox kayıtları DB'den alınıp publish ediliyor.
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;
    private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${payment-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${payment-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {

        Optional<List<OrderOutboxMessage>> outboxMessagesResponse = orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.STARTED);

        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderOutboxMessage with ids: {}, sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage -> outboxMessage.getId().toString())
                            .collect(Collectors.joining(",")));

            outboxMessages.forEach(outboxMessage -> paymentResponseMessagePublisher.publish(outboxMessage, orderOutboxHelper::updateOutboxStatus));
            log.info("{} OrderOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }

}

