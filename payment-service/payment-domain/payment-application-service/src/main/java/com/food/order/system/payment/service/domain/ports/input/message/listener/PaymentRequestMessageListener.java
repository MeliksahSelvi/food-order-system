package com.food.order.system.payment.service.domain.ports.input.message.listener;

import com.food.order.system.payment.service.domain.dto.PaymentRequest;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
 * Order bounded context'i üzerinde order create ediliği zaman oluşan eventi dinleyen payment messaging yapısının çağıracağı input portu.
 * Implementation'u (primary adapter) yine application-service katmanında olacak.
 * */
public interface PaymentRequestMessageListener {

    void completePayment(PaymentRequest paymentRequest);

    void cancelPayment(PaymentRequest paymentRequest);
}
