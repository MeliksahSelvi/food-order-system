package com.food.order.system.restaurant.service.common.messaging.kafka.producer;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {

    void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback);
}
