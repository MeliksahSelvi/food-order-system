package com.food.order.system.payment.service.domain.outbox.scheduler;

import com.food.order.system.outbox.OutboxScheduler;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.payment.service.domain.outbox.model.OrderOutboxMessage;
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
 * @Created 23.12.2023
 */

/*
 * Lifecycle'ını tamamlamış payment outbox adımlarını her gece yarısında temizleyen ve tablosunun gereksiz büyümesini engelleyen scheduler
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxCleanerScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;

    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

        if (outboxMessagesResponse.isPresent()) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderOutboxMessage for clean-up. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream().map(OrderOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));

            orderOutboxHelper.deleteByOutboxStatus(OutboxStatus.COMPLETED);
            log.info("{} OrderOutboxMessage deleted!", outboxMessages.size());
        }
    }
}
