package com.food.order.system.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;

/**
 * @Author mselvi
 * @Created 25.12.2023
 */

/*
* Aynı anda tek bir message okumak isteyen consumer'lar için
* */
public interface KafkaSingleItemConsumer<T extends SpecificRecordBase> {
    void receive(T message, String key, Integer partition, Long offset);
}
