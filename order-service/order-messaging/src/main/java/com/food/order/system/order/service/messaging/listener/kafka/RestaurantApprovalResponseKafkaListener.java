package com.food.order.system.order.service.messaging.listener.kafka;

import com.food.order.system.domain.event.payload.RestaurantOrderEventPayload;
import com.food.order.system.domain.valueobject.OrderApprovalStatus;
import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.messaging.DebeziumOp;
import com.food.order.system.order.service.domain.exception.OrderNotFoundException;
import com.food.order.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import debezium.restaurant.order_outbox.Envelope;
import debezium.restaurant.order_outbox.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

import static com.food.order.system.order.service.domain.entity.Order.FAILURE_MESSAGES_DELIMITER;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

/*
 * restaurant bounded context'i içinde approval işlemi sonucunda order işleminin approval veya reject edildiğini
 * kontrol edip cevaba göre domain layer'daki input portlarını triggerlayacak olan kafka listener
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<Envelope> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<Envelope> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of restaurant approval responses received!",
                messages.stream().filter(message -> message.getBefore() == null &&
                        DebeziumOp.CREATE.getValue().equals(message.getOp())).toList());

        messages.forEach(avroModel -> {
            if (avroModel.getBefore() == null && DebeziumOp.CREATE.getValue().equals(avroModel.getOp())) {
                log.info("Incoming message in PaymentResponseKafkaListener: {}", avroModel);
                Value responseAvroModel = avroModel.getAfter();
                RestaurantOrderEventPayload restaurantOrderEventPayload = kafkaMessageHelper.
                        getOrderEventPayload(responseAvroModel.getPayload(), RestaurantOrderEventPayload.class);
                try {
                    if (OrderApprovalStatus.APPROVED.name().equals(restaurantOrderEventPayload.getOrderApprovalStatus())) {
                        log.info("Processing approved order for order id: {}", restaurantOrderEventPayload.getOrderId());
                        restaurantApprovalResponseMessageListener.orderApproved(
                                orderMessagingDataMapper.approvalResponseAvroModelToRestaurantApprovalResponse(
                                        restaurantOrderEventPayload,
                                        responseAvroModel));
                    } else if (OrderApprovalStatus.REJECTED.name().equals(restaurantOrderEventPayload.getOrderApprovalStatus())) {
                        log.info("Processing rejected order for order id: {}, with failure messages: {}", restaurantOrderEventPayload.getOrderId(),
                                String.join(FAILURE_MESSAGES_DELIMITER, restaurantOrderEventPayload.getFailureMessages()));
                        restaurantApprovalResponseMessageListener.orderRejected(
                                orderMessagingDataMapper.approvalResponseAvroModelToRestaurantApprovalResponse(
                                        restaurantOrderEventPayload,
                                        responseAvroModel));
                    }

                } catch (OptimisticLockingFailureException e) {
                    log.error("Caught optimistic locking exception in RestaurantApprovalResponseKafkaListener for order id: {}",
                            restaurantOrderEventPayload.getOrderId());
                } catch (OrderNotFoundException e) {
                    log.error("No order found for order id: {}", restaurantOrderEventPayload.getOrderId());
                }catch (DataAccessException e){
                    SQLException sqlException = (SQLException) e.getRootCause();
                    if (sqlException != null && sqlException.getSQLState() != null &&
                            PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {

                        log.error("Caught unique constraint exception with sql state: {} " +
                                        " in RestaurantApprovalResponseKafkaListener for order id: {}",
                                sqlException.getSQLState(), restaurantOrderEventPayload.getOrderId());
                    }
                }
            }

        });
    }
}
