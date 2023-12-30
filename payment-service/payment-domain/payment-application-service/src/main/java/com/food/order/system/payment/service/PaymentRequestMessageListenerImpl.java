package com.food.order.system.payment.service;

import com.food.order.system.payment.service.dto.PaymentRequest;
import com.food.order.system.payment.service.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Bu listener (primary adapter) order bounded context'i tarafından gönderilen eventi takip eden payment messaging yapısının
 * çağıracağı input portunun implementation'u
 * */
@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistPayment(paymentRequest);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistCancelPayment(paymentRequest);
    }
}
