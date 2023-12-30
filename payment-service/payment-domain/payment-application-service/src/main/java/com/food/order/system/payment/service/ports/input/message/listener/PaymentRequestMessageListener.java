package com.food.order.system.payment.service.ports.input.message.listener;

import com.food.order.system.payment.service.dto.PaymentRequest;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

/*
 * Order bounded context'i üzerinde order create ediliği zaman oluşan ödeme işlemi talebini dinleyen payment messaging yapısının çağıracağı input portu.
 * Implementation'u (primary adapter) yine application-service katmanında olacak.
 * */
public interface PaymentRequestMessageListener {

    void completePayment(PaymentRequest paymentRequest);

    void cancelPayment(PaymentRequest paymentRequest);
}
