package com.food.order.system.restaurant.service.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.order.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.order.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.order.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Bir order approve edildiğinde order-service bounded context'ine OrderApprovedEvent eventini publish yapacak secondary adapter
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovedKafkaMessagePublisher implements OrderApprovedMessagePublisher {

    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;

    @Override
    public void publish(OrderApprovedEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();
        log.info("Received OrderApprovedEvent for order id: {}", orderId);

        try {
            RestaurantApprovalResponseAvroModel message = restaurantMessagingDataMapper.
                    orderApprovedEventToRestaurantApprovalResponseAvroModel(domainEvent);

            kafkaProducer.send(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                    orderId,
                    message,
                    kafkaMessageHelper.getKafkaCallback(
                            restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                            message,
                            orderId,
                            "RestaurantApprovalResponseAvroModel"));

            log.info("RestaurantApprovalResponseAvroModel sent to Kafka at: {}", System.nanoTime());

        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel message" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
