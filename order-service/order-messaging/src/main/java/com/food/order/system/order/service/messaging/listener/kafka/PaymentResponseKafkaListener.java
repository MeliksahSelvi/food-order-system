package com.food.order.system.order.service.messaging.listener.kafka;

import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
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
 * @Created 18.12.2023
 */

/*
 * payment-service bounded context'i içinde bir event çağrıldığında yakalayıp o cevaba göre domain layer'daki input portlarını
 * triggerlayacak olan kafka listener
 * */
@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentResponseAvroModel -> {
            switch (paymentResponseAvroModel.getPaymentStatus()) {
                case COMPLETED -> {
                    log.info("Processing successful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                    paymentResponseMessageListener.paymentCompleted(
                            orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
                }
                case CANCELLED, FAILED -> {
                    log.info("Processing unsuccessful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                    paymentResponseMessageListener.paymentCancelled(
                            orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
                }
            }
        });
    }
}
