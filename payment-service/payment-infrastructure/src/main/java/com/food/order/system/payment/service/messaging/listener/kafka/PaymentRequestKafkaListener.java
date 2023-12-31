package com.food.order.system.payment.service.messaging.listener.kafka;

import com.food.order.system.payment.service.common.messaging.kafka.consumer.KafkaConsumer;
import com.food.order.system.payment.service.common.messaging.kafka.model.PaymentRequestAvroModel;
import com.food.order.system.payment.service.dto.PaymentRequest;
import com.food.order.system.payment.service.exception.PaymentApplicationServiceException;
import com.food.order.system.payment.service.exception.PaymentNotFoundException;
import com.food.order.system.payment.service.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.order.system.payment.service.valueobject.PaymentOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * order-service bounded context'i içinde ödeme talebi içeren bir event fırlatıldığında siparişin durumuna göre
 * domain layer'daki input portlarını tetikleyecek olan secondary adapter
 * */
@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;


    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${payment-service.payment-request-topic-name}")
    public void receive(@Payload List<PaymentRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment requests received with keys:{}, partitions:{} and offsets:{}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());


        messages.forEach(paymentRequestAvroModel -> {
            try {
                switch (paymentRequestAvroModel.getPaymentOrderStatus()) {
                    case PENDING -> {
                        log.info("Processing payment for order id: {}", paymentRequestAvroModel.getOrderId());
                        paymentRequestMessageListener.completePayment(createPaymentRequest(paymentRequestAvroModel));
                    }
                    case CANCELLED -> {
                        log.info("Cancelling payment for order id: {}", paymentRequestAvroModel.getOrderId());
                        paymentRequestMessageListener.cancelPayment(createPaymentRequest(paymentRequestAvroModel));
                    }
                }
            } catch (DataAccessException e) {
                /*
                 * Normalde OptimisticLockingFailureException yakalamak istiyoruz fakat bu try bloğunda veri güncelleme işlemi olmadığından
                 * bu hatayı alamıyoruz.
                 * */
                SQLException sqlException = (SQLException) e.getRootCause();
                if (sqlException != null && sqlException.getSQLState() != null &&
                        PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {

                    log.error("Caught unique constraint exception with sql state: {} " +
                                    " in PaymentRequestKafkaListener for order id: {}",
                            sqlException.getSQLState(), paymentRequestAvroModel.getOrderId());
                } else {
                    throw new PaymentApplicationServiceException("Throwing DataAccessException in" +
                            " PaymentRequestKafkaListener: " + e.getMessage(), e);
                }
            } catch (PaymentNotFoundException e) {
                log.error("No payment found for order id: {}", paymentRequestAvroModel.getOrderId());
            } catch (Exception e) {
                throw new PaymentApplicationServiceException("Throwing Unexpected Exception in" +
                        " PaymentRequestKafkaListener: " + e.getMessage(), e);
            }
        });

    }

    private PaymentRequest createPaymentRequest(PaymentRequestAvroModel avroModel) {
        return PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(avroModel.getSagaId())
                .customerId(avroModel.getCustomerId())
                .orderId(avroModel.getOrderId())
                .price(avroModel.getPrice())
                .createdAt(avroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(avroModel.getPaymentOrderStatus().name()))
                .build();
    }
}
