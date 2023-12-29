package com.food.order.system.order.service.outbox.scheduler.approval;

import com.food.order.system.order.service.outbox.common.OutboxScheduler;
import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.order.service.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.food.order.system.order.service.saga.SagaStatus;
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
 * Burada restaurant service ile iletişim kurması gereken saga steplerini ve outbox steplerini DBden alıyoruz.
 * Buradaki tek durum Saga adımı OrderStatus paid'e eşit olan Processing SagaStatus'udur.
 * sonra bu saga steplerini publish ediyoruz ve bu işlem başarılı olursa durumunu güncelliyoruz.
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalOutboxScheduler implements OutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> outboxMessagesResponse = approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatuses(
                OutboxStatus.STARTED,
                SagaStatus.PROCESSING);

        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<OrderApprovalOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderApprovalOutboxMessage with ids: {}, sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage -> outboxMessage.getId().toString())
                            .collect(Collectors.joining(",")));

            outboxMessages.forEach(outboxMessage -> restaurantApprovalRequestMessagePublisher.publish(outboxMessage, this::updateOutboxStatus));
            log.info("{} OrderApprovalOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }

    /*
     * RestaurantApprovalRequestMessagePublisher çalıştığı zaman başarılı bir şekilde publish edildiği durumda
     * ilgili outbox message'sinin güncellenmesini sağlıyoruz.Eğer başarılı bir şekilde publish edilmezse
     * update yapmıyoruz.
     * */
    private void updateOutboxStatus(OrderApprovalOutboxMessage orderApprovalOutboxMessage, OutboxStatus outboxStatus) {
        orderApprovalOutboxMessage.setOutboxStatus(outboxStatus);
        approvalOutboxHelper.save(orderApprovalOutboxMessage);
        log.info("OrderApprovalOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }
}
