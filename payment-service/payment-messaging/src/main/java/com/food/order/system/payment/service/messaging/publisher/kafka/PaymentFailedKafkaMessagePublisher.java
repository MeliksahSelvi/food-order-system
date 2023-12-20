package com.food.order.system.payment.service.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.order.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.order.system.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.order.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Bir payment fail olduğunda order-service bounded context'ine PaymentFailedEvent eventini publish yapacak secondary adapter
 * */
@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentFailedKafkaMessagePublisher implements PaymentFailedMessagePublisher {

    private final PaymentServiceConfigData paymentServiceConfigData;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    @Override
    public void publish(PaymentFailedEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderId().getValue().toString();
        log.info("Received PaymentFailedEvent for order id: {}", orderId);

        try {
            PaymentResponseAvroModel message = paymentMessagingDataMapper.paymentFailedEventToPaymentResponseAvroModel(domainEvent);
            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
                    orderId,
                    message,
                    kafkaMessageHelper.getKafkaCallback(paymentServiceConfigData.getPaymentResponseTopicName(),
                            message,
                            orderId,
                            "PaymentResponseAvroModel"));
            log.info("PaymentResponseAvroModel sent to Kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending PaymentResponseAvroModel message" +
                    " to Kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
