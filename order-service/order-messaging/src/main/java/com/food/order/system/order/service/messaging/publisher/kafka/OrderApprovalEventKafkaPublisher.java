package com.food.order.system.order.service.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.order.service.domain.config.OrderServiceConfigData;
import com.food.order.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.order.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.order.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

/*
 * bu secondary adapter Bir order işleminde ödeme yapıldığında Order paid eventi oluşacağı zaman tetiklenen outport portunu implement ediyor.
 * */
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderApprovalEventKafkaPublisher implements RestaurantApprovalRequestMessagePublisher {

    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;

    @Override
    public void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                        BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback) {

        OrderApprovalEventPayload orderApprovalEventPayload = kafkaMessageHelper.
                getOrderEventPayload(orderApprovalOutboxMessage.getPayload(), OrderApprovalEventPayload.class);

        String sagaId = orderApprovalOutboxMessage.getSagaId().toString();
        log.info("Received OrderApprovalOutboxMessage for order id: {} and saga id: {}",
                orderApprovalEventPayload.getOrderId(), sagaId);

        try {
            RestaurantApprovalRequestAvroModel message = orderMessagingDataMapper
                    .orderApprovalEventToRestaurantApprovalRequestAvroModel(sagaId, orderApprovalEventPayload);

            kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    sagaId,
                    message,
                    kafkaMessageHelper
                            .getKafkaCallback(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                                    message,
                                    orderApprovalOutboxMessage,
                                    outboxCallback,
                                    orderApprovalEventPayload.getOrderId()));

            log.info("OrderApprovalEventPayload sent to Kafka for order id: {} and saga id: {}",
                    orderApprovalEventPayload.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending OrderApprovalEventPayload " +
                            " to kafka with order id: {} and saga id: {} error: {}",
                    orderApprovalEventPayload.getOrderId(), sagaId, e.getMessage());
        }
    }
}

