package com.food.order.system.order.service.domain.ports.input.message.listener.payment;

import com.food.order.system.order.service.domain.dto.message.PaymentResponse;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Payment işlemi yapıldığında oluşan sonucu dinleyen input portu.Implementation'u (primary adapter) yine application-service'de olacak.
 * Domain event listener'lar application service katmanında olur. Aynı zamanda bu event listener'lar domain event'ler tarafından tetiklenirler.
 * */
public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}
