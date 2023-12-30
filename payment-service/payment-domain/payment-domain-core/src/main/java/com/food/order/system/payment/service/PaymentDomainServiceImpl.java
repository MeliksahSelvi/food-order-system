package com.food.order.system.payment.service;

import com.food.order.system.payment.service.common.DomainConstants;
import com.food.order.system.payment.service.entity.CreditEntry;
import com.food.order.system.payment.service.entity.CreditHistory;
import com.food.order.system.payment.service.entity.Payment;
import com.food.order.system.payment.service.event.PaymentCancelledEvent;
import com.food.order.system.payment.service.event.PaymentCompletedEvent;
import com.food.order.system.payment.service.event.PaymentEvent;
import com.food.order.system.payment.service.event.PaymentFailedEvent;
import com.food.order.system.payment.service.valueobject.CreditHistoryId;
import com.food.order.system.payment.service.valueobject.Money;
import com.food.order.system.payment.service.valueobject.PaymentStatus;
import com.food.order.system.payment.service.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {


    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories,
                                                   List<String> failureMessages) {

        payment.validatePayment(failureMessages);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (failureMessages.isEmpty()) {
            log.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
        } else {
            log.info("Payment initiation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)), failureMessages);
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessages) {

        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

        if (failureMessages.isEmpty()) {
            log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
        } else {
            log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)), failureMessages);
        }
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            log.error("Customer with id: {} doesn't have enough credit for payment!",
                    payment.getCustomerId().getValue());
            failureMessages.add("Customer with id: " + payment.getCustomerId().getValue() +
                    " doesn't have enough credit for payment!");
        }

    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.substractCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories, TransactionType transactionType) {

        creditHistories.add(CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                .customerId(payment.getCustomerId())
                .transactionType(transactionType)
                .amount(payment.getPrice())
                .build());
    }

    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {

        Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);

        Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            log.error("Customer with id: {} doesn't have enough credit according to credit history",
                    creditEntry.getCustomerId().getValue());
            failureMessages.add("Customer with id: " + creditEntry.getCustomerId().getValue()
                    + " doesn't have enough credit according to credit history");
        }

        if (!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.substract(totalDebitHistory))) {
            log.error("Credit history total is not equal to current credit for customer id: {}!",
                    creditEntry.getCustomerId().getValue());
            failureMessages.add("Credit history total is not equal to current credit for customer id: "
                    + creditEntry.getCustomerId().getValue() + "!");
        }


    }

    private Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(creditHistory -> creditHistory.getTransactionType() == transactionType)
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }

    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }
}
