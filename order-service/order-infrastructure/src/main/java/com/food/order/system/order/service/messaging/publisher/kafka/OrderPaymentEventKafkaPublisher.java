package com.food.order.system.order.service.messaging.publisher.kafka;

import com.food.order.system.order.service.common.messaging.kafka.model.PaymentOrderStatus;
import com.food.order.system.order.service.common.messaging.kafka.model.PaymentRequestAvroModel;
import com.food.order.system.order.service.common.messaging.kafka.producer.KafkaProducer;
import com.food.order.system.order.service.common.messaging.kafka.producer.util.KafkaMessageHelper;
import com.food.order.system.order.service.config.OrderServiceConfigData;
import com.food.order.system.order.service.outbox.common.OutboxStatus;
import com.food.order.system.order.service.outbox.model.payment.OrderPaymentEventPayload;
import com.food.order.system.order.service.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.order.service.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
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
            PaymentRequestAvroModel message = createPaymentRequestAvroModel(sagaId, orderPaymentEventPayload);

            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    sagaId,
                    message,
                    kafkaMessageHelper
                            .getKafkaCallback(orderServiceConfigData.getPaymentRequestTopicName(),
                                    message,
                                    orderPaymentOutboxMessage,
                                    outboxCallback,
                                    orderPaymentEventPayload.getOrderId()));

            log.info("OrderPaymentEventPayload sent to Kafka for order id: {} and saga id: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending OrderPaymentEventPayload " +
                            " to kafka with order id: {} and saga id: {} error: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId, e.getMessage());
        }
    }

    private PaymentRequestAvroModel createPaymentRequestAvroModel(String sagaId, OrderPaymentEventPayload payload) {

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setCustomerId(payload.getCustomerId())
                .setOrderId(payload.getOrderId())
                .setPrice(payload.getPrice())
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(payload.getPaymentOrderStatus()))
                .build();
    }
}
