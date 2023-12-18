package com.food.order.system.order.service.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.order.service.domain.config.OrderServiceConfigData;
import com.food.order.system.order.service.domain.event.OrderCancelledEvent;
import com.food.order.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

/*
 * Bir order cancel edildiğinde payment-service bounded context'ine OrderCancelledEvent eventini publish yapacak component
 * */
@Component
@Slf4j
@RequiredArgsConstructor
public class CancelOrderKafkaMessagePublisher implements OrderCancelledPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final OrderKafkaMessageHelper orderKafkaMessageHelper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    @Override
    public void publish(OrderCancelledEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCancelledEvent for order id: {}", orderId);

        try {
            PaymentRequestAvroModel message = orderMessagingDataMapper.orderCancelledEventToPaymentRequestAvroModel(domainEvent);
            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    message,
                    orderKafkaMessageHelper.getKafkaCallback(orderServiceConfigData.getPaymentResponseTopicName()
                            , message, orderId, "PaymentRequestAvroModel")//todo class name
            );
            log.info("PaymentRequestAvroModel sent to Kafka for order id: {}", message.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
