package com.food.order.system.order.service.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.order.service.domain.config.OrderServiceConfigData;
import com.food.order.system.order.service.domain.event.OrderCreatedEvent;
import com.food.order.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

/*
 * Bir order create edildiğinde payment-service bounded context'ine OrderCreatedEvent eventini publish yapacak secondary adapter
 * */
@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCreatedEvent for order id: {}", orderId);

        try {
            PaymentRequestAvroModel message = orderMessagingDataMapper.orderCreatedEventToPaymentRequestAvroModel(domainEvent);
            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    message,
                    kafkaMessageHelper.getKafkaCallback(orderServiceConfigData.getPaymentResponseTopicName(),
                            message,orderId,"PaymentRequestAvroModel")//todo class name
            );
            log.info("PaymentRequestAvroModel sent to Kafka for order id: {}", message.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
