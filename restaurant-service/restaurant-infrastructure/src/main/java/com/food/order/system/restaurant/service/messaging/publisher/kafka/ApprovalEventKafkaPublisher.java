package com.food.order.system.restaurant.service.messaging.publisher.kafka;

import com.food.order.system.restaurant.service.common.messaging.kafka.model.OrderApprovalStatus;
import com.food.order.system.restaurant.service.common.messaging.kafka.model.RestaurantApprovalResponseAvroModel;
import com.food.order.system.restaurant.service.common.messaging.kafka.producer.KafkaProducer;
import com.food.order.system.restaurant.service.common.messaging.kafka.producer.util.KafkaMessageHelper;
import com.food.order.system.restaurant.service.config.RestaurantServiceConfigData;
import com.food.order.system.restaurant.service.outbox.common.OutboxStatus;
import com.food.order.system.restaurant.service.outbox.model.OrderEventPayload;
import com.food.order.system.restaurant.service.outbox.model.OrderOutboxMessage;
import com.food.order.system.restaurant.service.ports.output.message.publisher.ApprovalResponseMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalEventKafkaPublisher implements ApprovalResponseMessagePublisher {

    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;

    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
        OrderEventPayload orderEventPayload = kafkaMessageHelper.getOrderEventPayload(
                orderOutboxMessage.getPayload(), OrderEventPayload.class);

        String sagaId = orderOutboxMessage.getSagaId().toString();
        log.info("Received OrderOutboxMessage for order id: {} and saga id: {}",
                orderEventPayload.getOrderId(), sagaId);

        try {
            RestaurantApprovalResponseAvroModel message = createRestaurantApprovalResponseAvroModel(sagaId, orderEventPayload);

            String topicName = restaurantServiceConfigData.getRestaurantApprovalResponseTopicName();

            kafkaProducer.send(topicName, sagaId, message, kafkaMessageHelper.getKafkaCallback(
                    topicName,
                    message,
                    orderOutboxMessage,
                    outboxCallback,
                    orderEventPayload.getOrderId()));
        } catch (Exception e) {
            log.error("Error while sending OrderEventPayload " +
                            " to kafka with order id: {} and saga id: {} error: {}",
                    orderEventPayload.getOrderId(), sagaId, e.getMessage());
        }
    }

    private RestaurantApprovalResponseAvroModel createRestaurantApprovalResponseAvroModel(String sagaId, OrderEventPayload payload) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(payload.getOrderId())
                .setRestaurantId(payload.getRestaurantId())
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(payload.getOrderApprovalStatus()))
                .setFailureMessages(payload.getFailureMessages())
                .build();
    }
}

