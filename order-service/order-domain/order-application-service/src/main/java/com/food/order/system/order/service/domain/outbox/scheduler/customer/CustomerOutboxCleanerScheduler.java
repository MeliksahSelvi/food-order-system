package com.food.order.system.order.service.domain.outbox.scheduler.customer;

import com.food.order.system.order.service.domain.outbox.model.customer.CustomerOutboxMessage;
import com.food.order.system.outbox.OutboxScheduler;
import com.food.order.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * Lifecycle'ını tamamlamış customer outbox adımlarını her gece yarısında temizleyen ve tablosunun gereksiz büyümesini engelleyen scheduler
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerOutboxCleanerScheduler implements OutboxScheduler {

    private final CustomerOutboxHelper customerOutboxHelper;


    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<CustomerOutboxMessage>> outboxMessagesResponse = customerOutboxHelper.
                getCustomerOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

        if (outboxMessagesResponse.isPresent()) {
            List<CustomerOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} CustomerOutboxMessage for clean-up.", outboxMessages.size());
            customerOutboxHelper.deleteByOutboxStatus(OutboxStatus.COMPLETED);
            log.info("{} CustomerOutboxMessage deleted!", outboxMessages.size());
        }
    }
}
