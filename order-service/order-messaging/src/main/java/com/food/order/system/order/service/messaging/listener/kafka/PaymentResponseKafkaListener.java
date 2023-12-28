package com.food.order.system.order.service.messaging.listener.kafka;

import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.order.service.domain.dto.message.PaymentResponse;
import com.food.order.system.order.service.domain.exception.OrderNotFoundException;
import com.food.order.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
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
 * payment-service bounded context'i içinde ödeme işlemi ile alakalı bir event fırlatıldığında yakalayıp
 *  o cevaba göre domain layer'daki input portlarını triggerlayacak olan kafka listener
 * */
@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;

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
            try {
                switch (paymentResponseAvroModel.getPaymentStatus()) {
                    case COMPLETED -> {
                        log.info("Processing successful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                        paymentResponseMessageListener.paymentCompleted(createPaymentResponse(paymentResponseAvroModel));
                    }
                    case CANCELLED, FAILED -> {
                        log.info("Processing unsuccessful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                        paymentResponseMessageListener.paymentCancelled(createPaymentResponse(paymentResponseAvroModel));
                    }
                }
                /*
                 * optimisticlock exception şundan dolayı çıkabilir. Eğer PaymentResponseMessageListener input portu içinde
                 * işlemler yapılırken başka bir thread payment veya approval outbox işlemlerine müdahale etmeye çalışırsa
                 * bu hata alınır.Bunu version annotation'u sayesinde tutarsızlığın önüne geçeriz.
                 * */
            } catch (OptimisticLockingFailureException e) {
                log.error("Caught optimistic locking exception in PaymentResponseKafkaListener for order id: {}",
                        paymentResponseAvroModel.getOrderId());
            } catch (OrderNotFoundException e) {
                log.error("No order found for order id: {}", paymentResponseAvroModel.getOrderId());
            }
            /*
             * Bu 2 error'u şundan dolayı handle ettik.Bu mesajların tekrar kafkadan okunmasını istemiyoruz.
             * Handle etmediğimiz hatalar fırlatıldığı zaman bu consumer kafka'dan verileri tekrar okumaya çalışacaktır.
             * */
        });
    }

    private PaymentResponse createPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }
}
