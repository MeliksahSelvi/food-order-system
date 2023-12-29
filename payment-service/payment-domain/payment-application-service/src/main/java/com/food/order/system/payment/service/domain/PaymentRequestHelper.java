package com.food.order.system.payment.service.domain;

import com.food.order.system.payment.service.domain.dto.PaymentRequest;
import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.payment.service.domain.entity.CreditHistory;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
import com.food.order.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.order.system.payment.service.domain.exception.PaymentNotFoundException;
import com.food.order.system.payment.service.domain.outbox.common.OutboxStatus;
import com.food.order.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.order.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.order.system.payment.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.food.order.system.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.food.order.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.order.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.order.system.payment.service.domain.ports.output.repository.PaymentRepository;
import com.food.order.system.payment.service.domain.valueobject.CustomerId;
import com.food.order.system.payment.service.domain.valueobject.Money;
import com.food.order.system.payment.service.domain.valueobject.OrderId;
import com.food.order.system.payment.service.domain.valueobject.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

    @Transactional
    public void persistPayment(PaymentRequest paymentRequest) {
        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.COMPLETED)) {
            log.info("An outbox message with saga id: {} is already saved to database!",
                    paymentRequest.getSagaId());
            return;
        }
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = createPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories, failureMessages);
        persistEntities(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.persistOrderOutboxMessage(createOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentstatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId()));
    }

    @Transactional
    public void persistCancelPayment(PaymentRequest paymentRequest) {
        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.CANCELLED)) {
            log.info("An outbox message with saga id: {} is already saved to database!",
                    paymentRequest.getSagaId());
            return;
        }
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentOptional.isEmpty()) {
            log.error("Payment with order id: {} could not be found!", paymentRequest.getOrderId());
            throw new PaymentNotFoundException("Payment with order id: " + paymentRequest.getOrderId() +
                    " could not be found!");
        }
        Payment payment = paymentOptional.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages);
        persistEntities(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.persistOrderOutboxMessage(createOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentstatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId()));
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntryOptional = creditEntryRepository.findByCustomerId(customerId);
        if (creditEntryOptional.isEmpty()) {
            log.error("Could not find credit entry for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer: "
                    + customerId.getValue());
        }
        return creditEntryOptional.get();
    }

    private List<CreditHistory> getCreditHistories(CustomerId customerId) {
        Optional<List<CreditHistory>> optionalCreditHistories = creditHistoryRepository.findByCustomerId(customerId);
        if (optionalCreditHistories.isEmpty()) {
            log.error("Could not find credit history for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit history for customer: "
                    + customerId.getValue());
        }
        return optionalCreditHistories.get();
    }

    private void persistEntities(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }

    /*
     * burada save ettiğimiz orderoutboxmessage'ler scheduler ile publish ediliyor ve işlem bittiğinde callback methodunda
     * PaymentStatus.COMPLETED olarak işaretleniyor.publish edilen message kafkadan ack return olduğunda
     * order service consume etmeye başladığı zaman bir problem olursa order service aynı sagaId'ye sahip olan
     * başka bir event consume etmek isteyebilir.Biz de aslında DB'ye kaydedilmiş mesajı tekrar DB'ye kaydetmeden tekrar publish ediyoruz.
     * */
    private boolean publishIfOutboxMessageProcessedForPayment(PaymentRequest paymentRequest,
                                                              PaymentStatus paymentStatus) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(
                        UUID.fromString(paymentRequest.getSagaId()),
                        paymentStatus);
        if (orderOutboxMessage.isPresent()) {
            paymentResponseMessagePublisher.publish(orderOutboxMessage.get(), orderOutboxHelper::updateOutboxStatus);
            return true;
        }
        return false;
    }

    private Payment createPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();

    }

    private OrderEventPayload createOrderEventPayload(PaymentEvent paymentEvent) {
        return OrderEventPayload.builder()
                .paymentId(paymentEvent.getPayment().getId().getValue().toString())
                .customerId(paymentEvent.getPayment().getCustomerId().getValue().toString())
                .orderId(paymentEvent.getPayment().getOrderId().getValue().toString())
                .price(paymentEvent.getPayment().getPrice().getAmount())
                .createdAt(paymentEvent.getCreatedAt())
                .paymentStatus(paymentEvent.getPayment().getPaymentstatus().name())
                .failureMessages(paymentEvent.getFailureMessages())
                .build();
    }
}
