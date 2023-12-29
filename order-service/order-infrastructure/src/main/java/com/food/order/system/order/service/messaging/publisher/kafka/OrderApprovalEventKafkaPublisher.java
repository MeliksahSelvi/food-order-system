package com.food.order.system.order.service.messaging.publisher.kafka;

import com.food.order.system.order.service.common.messaging.kafka.model.Product;
import com.food.order.system.order.service.common.messaging.kafka.model.RestaurantApprovalRequestAvroModel;
import com.food.order.system.order.service.common.messaging.kafka.model.RestaurantOrderStatus;
import com.food.order.system.order.service.common.messaging.kafka.producer.KafkaProducer;
import com.food.order.system.order.service.common.messaging.kafka.producer.util.KafkaMessageHelper;
import com.food.order.system.order.service.config.OrderServiceConfigData;
import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.approval.OrderApprovalEventPayload;
import com.food.order.system.order.service.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.order.service.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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
            RestaurantApprovalRequestAvroModel message = createRestaurantApprovalRequestAvroModel(sagaId, orderApprovalEventPayload);

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

    private RestaurantApprovalRequestAvroModel createRestaurantApprovalRequestAvroModel(String sagaId,
                                                                                        OrderApprovalEventPayload payload) {

        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(payload.getOrderId())
                .setRestaurantId(payload.getRestaurantId())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(payload.getRestaurantOrderStatus()))
                .setProducts(payload.getProducts().stream()
                        .map(approvalProduct -> Product.newBuilder()
                                .setId(approvalProduct.getId())
                                .setQuantity(approvalProduct.getQuantity())
                                .build()).
                        collect(Collectors.toList()))
                .setPrice(payload.getPrice())
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .build();
    }
}

