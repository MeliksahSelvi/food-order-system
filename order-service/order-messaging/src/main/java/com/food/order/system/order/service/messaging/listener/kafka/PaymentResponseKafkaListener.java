package com.food.order.system.order.service.messaging.listener.kafka;

import com.food.order.system.domain.event.payload.PaymentOrderEventPayload;
import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.messaging.DebeziumOp;
import com.food.order.system.order.service.domain.exception.OrderNotFoundException;
import com.food.order.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import debezium.payment.order_outbox.Envelope;
import debezium.payment.order_outbox.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
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
public class PaymentResponseKafkaListener implements KafkaConsumer<Envelope> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaMessageHelper kafkaMessageHelper;

    /*
     * Burada list yerine tek tek de message okuyabiliriz. Fakat hata şansımız az ise list kullanmak daha verimli.
     * */
    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<Envelope> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment responses received!",
                messages.stream().filter(message -> message.getBefore() == null &&
                        DebeziumOp.CREATE.getValue().equals(message.getOp())).toList());

        messages.forEach(avroModel -> {
            if (avroModel.getBefore() == null && DebeziumOp.CREATE.getValue().equals(avroModel.getOp())) {
                log.info("Incoming message in PaymentResponseKafkaListener: {}", avroModel);
                Value paymentResponseAvroModel = avroModel.getAfter();
                PaymentOrderEventPayload paymentOrderEventPayload = kafkaMessageHelper.
                        getOrderEventPayload(paymentResponseAvroModel.getPayload(), PaymentOrderEventPayload.class);
                try {
                    if (PaymentStatus.COMPLETED.name().equals(paymentOrderEventPayload.getPaymentStatus())) {
                        log.info("Processing successful payment for order id: {}", paymentOrderEventPayload.getOrderId());
                        paymentResponseMessageListener.paymentCompleted(
                                orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentOrderEventPayload, paymentResponseAvroModel));
                    } else if (PaymentStatus.CANCELLED.name().equals(paymentOrderEventPayload.getPaymentStatus()) &&
                            PaymentStatus.FAILED.name().equals(paymentOrderEventPayload.getPaymentStatus())) {
                        log.info("Processing unsuccessful payment for order id: {}", paymentOrderEventPayload.getOrderId());
                        paymentResponseMessageListener.paymentCancelled(
                                orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentOrderEventPayload, paymentResponseAvroModel));
                    }

                    /*
                     * optimisticlock exception şundan dolayı çıkabilir. Eğer PaymentResponseMessageListener input portu içinde
                     * işlemler yapılırken başka bir thread payment veya approval outbox işlemlerine müdahale etmeye çalışırsa
                     * bu hata alınır.Bunu version annotation'u sayesinde tutarsızlığın önüne geçeriz.
                     * */
                } catch (OptimisticLockingFailureException e) {
                    log.error("Caught optimistic locking exception in PaymentResponseKafkaListener for order id: {}",
                            paymentOrderEventPayload.getOrderId());
                } catch (OrderNotFoundException e) {
                    log.error("No order found for order id: {}", paymentOrderEventPayload.getOrderId());
                } catch (DataAccessException e) {
                    SQLException sqlException = (SQLException) e.getRootCause();
                    if (sqlException != null && sqlException.getSQLState() != null &&
                            PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {

                        log.error("Caught unique constraint exception with sql state: {} " +
                                        " in PaymentResponseKafkaListener for order id: {}",
                                sqlException.getSQLState(), paymentOrderEventPayload.getOrderId());
                    }
                }
                /*
                 * Bu 2 error'u şundan dolayı handle ettik.Bu mesajların tekrar kafkadan okunmasını istemiyoruz.
                 * Handle etmediğimiz hatalar fırlatıldığı zaman bu consumer kafka'dan verileri tekrar okumaya çalışacaktır.
                 * 3 verimiz olduğunu düşünelim. 3.de bu hatalar alınırsa ilk 2 veri tekrar okunmasın diye bu kontrol işimize yaradı.
                 * */
            }
        });
    }
}
