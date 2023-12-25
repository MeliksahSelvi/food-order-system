package com.food.order.system.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

/**
 * @Author mselvi
 * @Created 17.12.2023
 */

/*
 * Aynı anda list şeklinde message'leri okumak isteyen consumer'lar için
 * */
public interface KafkaConsumer<T extends SpecificRecordBase> {
    void receive(List<T> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}
