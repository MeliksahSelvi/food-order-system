package com.food.order.system.order.service.common.messaging.kafka.producer.exception;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

public class KafkaProducerException extends RuntimeException{

    public KafkaProducerException(String message) {
        super(message);
    }
}
