package com.food.order.system.payment.service.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.kafka.order.avro.model.PaymentStatus;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.order.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.order.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.order.system.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
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
 * bu secondary adapter paid completed,failed ve cancelled eventleri oluşacağı zaman tetiklenen outport portunu implement ediyor.
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventKafkaPublisher implements PaymentResponseMessagePublisher {

    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {

        OrderEventPayload orderEventPayload = kafkaMessageHelper.getOrderEventPayload(
                orderOutboxMessage.getPayload(), OrderEventPayload.class);

        String sagaId = orderOutboxMessage.getSagaId().toString();
        log.info("Received OrderOutboxMessage for order id: {} and saga id: {}",
                orderEventPayload.getOrderId(), sagaId);

        try {
            PaymentResponseAvroModel message = createPaymentResponseAvroModel(sagaId, orderEventPayload);

            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
                    sagaId,
                    message,
                    kafkaMessageHelper.
                            getKafkaCallback(
                                    paymentServiceConfigData.getPaymentResponseTopicName(),
                                    message,
                                    orderOutboxMessage,
                                    outboxCallback,
                                    orderEventPayload.getOrderId()));

            log.info("OrderEventPayload sent to Kafka for order id: {} and saga id: {}",
                    orderEventPayload.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending OrderEventPayload " +
                            " to kafka with order id: {} and saga id: {} error: {}",
                    orderEventPayload.getOrderId(), sagaId, e.getMessage());
        }

    }

    private PaymentResponseAvroModel createPaymentResponseAvroModel(String sagaId, OrderEventPayload payload) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setPaymentId(payload.getPaymentId())
                .setCustomerId(payload.getCustomerId())
                .setOrderId(payload.getOrderId())
                .setPrice(payload.getPrice())
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(payload.getPaymentStatus()))
                .setFailureMessages(payload.getFailureMessages())
                .build();
    }
}
