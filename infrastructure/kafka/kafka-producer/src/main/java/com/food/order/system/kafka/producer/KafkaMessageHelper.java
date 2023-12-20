package com.food.order.system.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

@Slf4j
@Component
public class KafkaMessageHelper {

    public <T> BiConsumer<SendResult<String, T>, Throwable> getKafkaCallback(String responseTopicName,
                                                                             T message,
                                                                             String orderId,
                                                                             String avroModelName) {
        return (sendResult, ex) -> {
            if (ex == null) {
                RecordMetadata metadata = sendResult.getRecordMetadata();
                log.info("Received successful response from Kafka for order id: {}" +
                                " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                        orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp());
            } else {
                log.error("Error while sending {} message {} to topic {}", avroModelName,message.toString(), responseTopicName, ex);
            }
        };
    }
}
