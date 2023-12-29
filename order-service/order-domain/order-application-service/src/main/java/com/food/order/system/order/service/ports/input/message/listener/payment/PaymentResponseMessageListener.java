package com.food.order.system.order.service.ports.input.message.listener.payment;

import com.food.order.system.order.service.dto.message.PaymentResponse;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Payment bounded context'ine ödeme talebi geldiğinde oluşan response'a göre eventi dinleyen order messaging yapısının çağıracağı input portu.
 * Implementation'u (primary adapter) yine application-service'de olacak.
 * */
public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}
