package com.food.order.system.payment.service.domain;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.payment.service.domain.entity.CreditHistory;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.order.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
import com.food.order.system.payment.service.domain.event.PaymentFailedEvent;

import java.util.List;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories, List<String> failureMessages,
                                            DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher,
                                            DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);

    PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                          List<CreditHistory> creditHistories, List<String> failureMessages,
                                          DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher,
                                          DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);
}
