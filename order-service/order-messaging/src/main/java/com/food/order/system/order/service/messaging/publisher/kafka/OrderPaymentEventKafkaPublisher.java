package com.food.order.system.order.service.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.order.service.domain.config.OrderServiceConfigData;
import com.food.order.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.order.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.order.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

/*
 * bu secondary adapter Order create ve order cancel eventleri oluşacağı zaman tetiklenen outport portunu implement ediyor.
 * */
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderPaymentEventKafkaPublisher implements PaymentRequestMessagePublisher {

    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    @Override
    public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                        BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback) {

        OrderPaymentEventPayload orderPaymentEventPayload = kafkaMessageHelper.getOrderEventPayload(
                orderPaymentOutboxMessage.getPayload(), OrderPaymentEventPayload.class);

        String sagaId = orderPaymentOutboxMessage.getSagaId().toString();
        log.info("Received OrderPaymentOutboxMessage for order id: {} and saga id: {}",
                orderPaymentEventPayload.getOrderId(), sagaId);

        try {
            PaymentRequestAvroModel message = orderMessagingDataMapper
                    .orderPaymentEventToPaymentRequestAvroModel(sagaId, orderPaymentEventPayload);

            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    sagaId,
                    message,
                    kafkaMessageHelper
                            .getKafkaCallback(orderServiceConfigData.getPaymentRequestTopicName(),
                                    message,
                                    orderPaymentOutboxMessage,
                                    outboxCallback,
                                    orderPaymentEventPayload.getOrderId(), "PaymentRequestAvroModel"));

            log.info("OrderPaymentEventPayload sent to Kafka for order id: {} and saga id: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending OrderPaymentEventPayload " +
                            " to kafka with order id: {} and saga id: {} error: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId, e.getMessage());
        }
    }
}
