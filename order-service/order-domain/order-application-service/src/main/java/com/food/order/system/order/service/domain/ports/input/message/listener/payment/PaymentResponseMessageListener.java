package com.food.order.system.order.service.domain.ports.input.message.listener.payment;

import com.food.order.system.order.service.domain.dto.message.PaymentResponse;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Payment bounded context'i üzerinde ödeme işlemi yapıldığında oluşan sonucu dinleyen messaging yapısının çağıracağı input portu.
 * Implementation'u (primary adapter) yine application-service'de olacak.
 * */
public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}
