package com.food.order.system.payment.service;

import com.food.order.system.payment.service.dto.PaymentRequest;
import com.food.order.system.payment.service.exception.PaymentApplicationServiceException;
import com.food.order.system.payment.service.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.function.Function;

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
    private static final int MAX_EXECUTION = 100;//todo config ayarlarından verilebilir.


    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        processPayment(paymentRequestHelper::persistPayment, paymentRequest, "completePayment");
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        processPayment(paymentRequestHelper::persistPayment, paymentRequest, "cancelPayment");
    }

    private void processPayment(Function<PaymentRequest, Boolean> func, PaymentRequest paymentRequest, String methodName) {
        int execution = 1;
        boolean result;
        do {
            log.info("Executing {} operation for {} time for order id {}", methodName, execution, paymentRequest.getOrderId());
            try {
                result = func.apply(paymentRequest);
                execution++;
            } catch (OptimisticLockingFailureException e) {
                log.warn("Caught OptimisticLockingFailureException in {} with message {}!. Retrying for order id: {}!",
                        methodName, e.getMessage(), paymentRequest.getOrderId());
                result = false;
            }

        } while (!result && execution < MAX_EXECUTION);

        if (!result) {
            throw new PaymentApplicationServiceException("Could not complete " + methodName + " operation.Throwing exception!");
        }
    }
}
