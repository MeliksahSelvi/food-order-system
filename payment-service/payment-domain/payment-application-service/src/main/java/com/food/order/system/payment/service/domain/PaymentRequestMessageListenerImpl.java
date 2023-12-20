package com.food.order.system.payment.service.domain;

import com.food.order.system.payment.service.domain.dto.PaymentRequest;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
import com.food.order.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
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
        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistCancelPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    private void fireEvent(PaymentEvent paymentEvent) {
        log.info("Publishing payment event with payment id: {} and order id: {}",
                paymentEvent.getPayment().getId().getValue(),
                paymentEvent.getPayment().getOrderId().getValue());

        paymentEvent.fire();
    }
}
