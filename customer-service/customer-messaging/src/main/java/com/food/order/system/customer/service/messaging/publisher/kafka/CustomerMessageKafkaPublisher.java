package com.food.order.system.customer.service.messaging.publisher.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.customer.service.domain.config.CustomerServiceConfigData;
import com.food.order.system.customer.service.domain.exception.CustomerDomainException;
import com.food.order.system.customer.service.domain.outbox.model.CustomerEventPayload;
import com.food.order.system.customer.service.domain.outbox.model.CustomerOutboxMessage;
import com.food.order.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import com.food.order.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerMessageKafkaPublisher implements CustomerMessagePublisher {

    private final CustomerServiceConfigData customerServiceConfigData;
    private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(CustomerOutboxMessage customerOutboxMessage, BiConsumer<CustomerOutboxMessage, OutboxStatus> outboxCallback) {
        CustomerEventPayload customerEventPayload = convertPayload(customerOutboxMessage.getPayload());

        String sagaId = customerOutboxMessage.getSagaId().toString();
        log.info("Received CustomerOutboxMessage for customer id: {} and saga id: {}",
                customerEventPayload.getCustomerId(), sagaId);

        try {
            CustomerAvroModel message = createCustomerAvroModel(sagaId, customerEventPayload);

            String topicName = customerServiceConfigData.getCustomerRequestTopicName();

            kafkaProducer.send(topicName, sagaId, message, getKafkaCallback(
                    topicName,
                    message,
                    customerOutboxMessage,
                    outboxCallback));
        } catch (Exception e) {
            log.error("Error while sending CustomerEventPayload " +
                            " to kafka with customer id: {} and saga id: {} error: {}",
                    customerEventPayload.getCustomerId(), sagaId, e.getMessage());
        }
    }

    private CustomerEventPayload convertPayload(String payload) {
        try {
            return objectMapper.readValue(payload, CustomerEventPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Could not read CustomerEventPayload object!", e);
            throw new CustomerDomainException("Could not read CustomerEventPayload object!", e);
        }
    }

    private BiConsumer<SendResult<String, CustomerAvroModel>, Throwable>
    getKafkaCallback(String responseTopicName, CustomerAvroModel avroModel, CustomerOutboxMessage outboxMessage,
                     BiConsumer<CustomerOutboxMessage, OutboxStatus> outboxCallback) {
        return (result, ex) -> {
            if (ex == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received successful response from Kafka for customer id: {}" +
                                " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                        avroModel.getId(),
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp());
                outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);
            } else {
                log.error("Error while sending {} with message: {} and outbox type: {} to topic {}",
                        avroModel.getClass().getSimpleName(), avroModel, outboxMessage.getClass().getName(), responseTopicName, ex);
                outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
            }
        };
    }

    private CustomerAvroModel createCustomerAvroModel(String sagaId, CustomerEventPayload payload) {
        return CustomerAvroModel.newBuilder()
                .setId(payload.getCustomerId())
                .setSagaId(sagaId)
                .setUsername(payload.getUsername())
                .setFirstName(payload.getFirstName())
                .setLastName(payload.getLastName())
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .build();
    }
}