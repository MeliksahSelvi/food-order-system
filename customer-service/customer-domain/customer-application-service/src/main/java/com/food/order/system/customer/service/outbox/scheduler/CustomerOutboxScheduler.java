package com.food.order.system.customer.service.outbox.scheduler;

import com.food.order.system.customer.service.common.OutboxScheduler;
import com.food.order.system.customer.service.common.OutboxStatus;
import com.food.order.system.customer.service.outbox.model.CustomerOutboxMessage;
import com.food.order.system.customer.service.ports.output.message.publisher.CustomerMessagePublisher;
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
public class CustomerOutboxScheduler implements OutboxScheduler {

    private final CustomerOutboxHelper customerOutboxHelper;
    private final CustomerMessagePublisher customerMessagePublisher;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${customer-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${customer-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<CustomerOutboxMessage>> outboxMessagesResponse = customerOutboxHelper.
                getCustomerOutboxMessageByOutboxStatus(OutboxStatus.STARTED);

        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<CustomerOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} CustomerOutboxMessage with ids: {}, sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream()
                            .map(outboxMessage -> outboxMessage.getId().toString())
                            .collect(Collectors.joining(",")));

            outboxMessages.forEach(orderOutboxMessage -> customerMessagePublisher.publish(
                    orderOutboxMessage, customerOutboxHelper::updateOutboxStatus));
            log.info("{} CustomerOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }
}