package com.food.order.system.restaurant.service.messaging.listener.kafka;

import com.food.order.system.restaurant.service.common.messaging.kafka.consumer.KafkaConsumer;
import com.food.order.system.restaurant.service.common.messaging.kafka.model.RestaurantApprovalRequestAvroModel;
import com.food.order.system.restaurant.service.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.service.entity.Product;
import com.food.order.system.restaurant.service.exception.RestaurantApplicationServiceException;
import com.food.order.system.restaurant.service.exception.RestaurantNotFoundException;
import com.food.order.system.restaurant.service.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import com.food.order.system.restaurant.service.valueobject.ProductId;
import com.food.order.system.restaurant.service.valueobject.RestaurantOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Ödemesi yapılan sipariş için approval isteği atıldığı zaman ilgili isteği dinleyip
 * domain layer'daki input portlarını triggerlayacak olan kafka listener
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalRequestKafkaListener implements KafkaConsumer<RestaurantApprovalRequestAvroModel> {

    private final RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${restaurant-service.restaurant-approval-request-topic-name}")
    public void receive(@Payload List<RestaurantApprovalRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of orders approval requests received with keys: {}, partitions: {} and offsets: {}" +
                        ", sending for restaurant approval",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(restaurantApprovalRequestAvroModel -> {
            try {
                log.info("Processing order approval event at: {}", System.nanoTime());
                restaurantApprovalRequestMessageListener.approveOrder(createRestaurantApprovalRequest(restaurantApprovalRequestAvroModel));
            } catch (DataAccessException e) {
                SQLException sqlException = (SQLException) e.getRootCause();
                if (sqlException != null && sqlException.getSQLState() != null &&
                        PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {

                    log.error("Caught unique constraint exception with sql state: {} " +
                                    " in RestaurantApprovalRequestKafkaListener for order id: {}",
                            sqlException.getSQLState(), restaurantApprovalRequestAvroModel.getOrderId());
                } else {
                    throw new RestaurantApplicationServiceException("Throwing DataAccessException in" +
                            " RestaurantApprovalRequestKafkaListener: " + e.getMessage(), e);
                }
            } catch (RestaurantNotFoundException e) {
                log.error("No restaurant found for restaurant id: {} and order id: {}",
                        restaurantApprovalRequestAvroModel.getRestaurantId(),
                        restaurantApprovalRequestAvroModel.getOrderId());
            }

        });
    }

    private RestaurantApprovalRequest createRestaurantApprovalRequest(
            RestaurantApprovalRequestAvroModel requestAvroModel) {

        return RestaurantApprovalRequest.builder()
                .id(requestAvroModel.getId())
                .sagaId(requestAvroModel.getSagaId())
                .restaurantId(requestAvroModel.getRestaurantId())
                .orderId(requestAvroModel.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(requestAvroModel.getRestaurantOrderStatus().name()))
                .products(requestAvroModel.getProducts().stream()
                        .map(avroModel ->
                                Product.builder()
                                        .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(requestAvroModel.getPrice())
                .createdAt(requestAvroModel.getCreatedAt())
                .build();
    }
}
