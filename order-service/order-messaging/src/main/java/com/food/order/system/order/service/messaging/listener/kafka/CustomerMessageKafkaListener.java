package com.food.order.system.order.service.messaging.listener.kafka;

import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.messaging.DebeziumOp;
import com.food.order.system.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.order.system.outbox.customer.model.CustomerEventPayload;
import debezium.customer.customer_outbox.Envelope;
import debezium.customer.customer_outbox.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerMessageKafkaListener implements KafkaConsumer<Envelope> {

    private final CustomerMessageListener customerMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}",
            topics = "${order-service.customer-response-topic-name}")
    public void receive(@Payload List<Envelope> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        /*
        * Sadece save işlemlerini alıyoruz.update almıyoruz.
        * */
        log.info("{} number of payment responses received!",
                messages.stream().filter(message -> message.getBefore() == null &&
                        DebeziumOp.CREATE.getValue().equals(message.getOp())).toList());

        messages.forEach(avroModel -> {
                    if (avroModel.getBefore() == null && DebeziumOp.CREATE.getValue().equals(avroModel.getOp())){
                        log.info("Incoming message in CustomerMessageKafkaListener: {}", avroModel);
                        Value customerAvroModel = avroModel.getAfter();
                        CustomerEventPayload customerEventPayload = kafkaMessageHelper.
                                getOrderEventPayload(customerAvroModel.getPayload(), CustomerEventPayload.class);
                        customerMessageListener.customerCreated(orderMessagingDataMapper.
                                customerAvroModelToCustomerModel(customerEventPayload,customerAvroModel));
                    }
                });
    }
}
