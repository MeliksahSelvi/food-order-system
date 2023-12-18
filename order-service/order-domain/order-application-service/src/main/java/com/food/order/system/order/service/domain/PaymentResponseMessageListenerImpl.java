package com.food.order.system.order.service.domain;

import com.food.order.system.order.service.domain.dto.message.PaymentResponse;
import com.food.order.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Service
@Slf4j
@Validated//neden eklendi ve bu class neden order application service impl gibi package privacy değil
/*
 * bu listener (primary adapter) payment  bounded context'i tarafından gönderilen eventi takip eden messaging yapısının
 * çağıracağı input portunun implementation'u
 * */
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {
    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {

    }
}
