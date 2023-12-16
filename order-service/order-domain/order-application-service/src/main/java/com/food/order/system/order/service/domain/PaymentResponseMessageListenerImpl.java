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
@Validated//neden eklendi ve bu class neden public
/*
 * bu listener (primary adapter) payment ve restaurant bounded context'ler içindeki domain eventler tarafından triggerlanacak.aslında saga pattern yapacak.
 * */
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {
    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {

    }
}
