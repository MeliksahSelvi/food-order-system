package com.food.order.system.order.service.messaging.listener.kafka;

import com.food.order.system.order.service.common.messaging.kafka.consumer.KafkaConsumer;
import com.food.order.system.order.service.common.messaging.kafka.model.CustomerAvroModel;
import com.food.order.system.order.service.dto.message.CustomerModel;
import com.food.order.system.order.service.ports.input.message.listener.customer.CustomerMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.order.system.order.service.common.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerMessageKafkaListener implements KafkaConsumer<CustomerAvroModel> {

    private final CustomerMessageListener customerMessageListener;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}",
            topics = "${order-service.customer-request-topic-name}")
    public void receive(@Payload List<CustomerAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(customerAvroModel ->
                customerMessageListener.customerCreated(CustomerModel.builder()
                        .id(customerAvroModel.getId())
                        .username(customerAvroModel.getUsername())
                        .firstName(customerAvroModel.getFirstName())
                        .lastName(customerAvroModel.getLastName())
                        .createdAt(ZonedDateTime.ofInstant(customerAvroModel.getCreatedAt(), ZoneId.of(UTC)))
                        .build()));
    }
}
