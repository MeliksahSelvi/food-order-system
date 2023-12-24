package com.food.order.system.customer.service.messaging.mapper;

import com.food.order.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.order.system.outbox.customer.model.CustomerEventPayload;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Component
public class CustomerMessagingDataMapper {

    public CustomerAvroModel customerEventPayloadToCustomerAvroModel(String sagaId, CustomerEventPayload payload) {
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
