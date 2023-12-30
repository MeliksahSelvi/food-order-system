package com.food.order.system.payment.service;

import com.food.order.system.payment.service.entity.CreditEntry;
import com.food.order.system.payment.service.entity.CreditHistory;
import com.food.order.system.payment.service.entity.Payment;
import com.food.order.system.payment.service.event.PaymentEvent;

import java.util.List;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories, List<String> failureMessages);

    PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                          List<CreditHistory> creditHistories, List<String> failureMessages);
}
