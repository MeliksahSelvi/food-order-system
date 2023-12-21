package com.food.order.system.payment.service.domain;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.payment.service.domain.dto.PaymentRequest;
import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.payment.service.domain.entity.CreditHistory;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
import com.food.order.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.order.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.order.system.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.order.system.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.food.order.system.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.order.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.order.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.order.system.payment.service.domain.ports.output.repository.PaymentRepository;
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
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentCompletedMessagePublisher paymentCompletedEventDomainEventPublisher;
    private final PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher;

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories, failureMessages,
                paymentCompletedEventDomainEventPublisher, paymentFailedEventDomainEventPublisher);
        persistEntities(payment, creditEntry, creditHistories, failureMessages);
        return paymentEvent;
    }


    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentOptional.isEmpty()) {
            log.error("Payment with order id: {} could not be found!", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Payment with order id: " + paymentRequest.getOrderId() +
                    " could not be found!");
        }
        Payment payment = paymentOptional.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages,
                paymentCancelledEventDomainEventPublisher, paymentFailedEventDomainEventPublisher);
        persistEntities(payment, creditEntry, creditHistories, failureMessages);
        return paymentEvent;
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
}
