package com.food.order.system.order.service.messaging.listener.kafka;

import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.order.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.food.order.system.order.service.domain.entity.Order.FAILURE_MESSAGES_DELIMITER;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

/*
 * restaurant-service bounded context'i içinde bir event fırlatıldığında yakalayıp o cevaba göre domain layer'daki input portlarını
 * triggerlayacak olan kafka listener
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;


    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of restaurant approval responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(responseAvroModel -> {
            switch (responseAvroModel.getOrderApprovalStatus()) {
                case APPROVED -> {
                    log.info("Processing approved order for order id: {}", responseAvroModel.getOrderId());
                    restaurantApprovalResponseMessageListener.orderApproved(
                            orderMessagingDataMapper.approvalResponseAvroModelToRestaurantApprovalResponse(responseAvroModel));
                }
                case REJECTED -> {
                    log.info("Processing rejected order for order id: {}, with failure messages: {}", responseAvroModel.getOrderId(),
                            String.join(FAILURE_MESSAGES_DELIMITER, responseAvroModel.getFailureMessages()));
                    restaurantApprovalResponseMessageListener.orderRejected(
                            orderMessagingDataMapper.approvalResponseAvroModelToRestaurantApprovalResponse(responseAvroModel));
                }
            }
        });
    }
}
