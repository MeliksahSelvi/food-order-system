package com.food.order.system.kafka.producer.exception;

/**
 * @Author mselvi
 * @Created 17.12.2023
 */

public class KafkaProducerException extends RuntimeException{

    public KafkaProducerException(String message) {
        super(message);
    }

}
