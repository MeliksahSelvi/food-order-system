package com.food.order.system.order.service.domain;

import com.food.order.system.order.service.domain.dto.message.PaymentResponse;
import com.food.order.system.order.service.domain.event.OrderPaidEvent;
import com.food.order.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.food.order.system.order.service.domain.entity.Order.FAILURE_MESSAGES_DELIMITER;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Validated//todo neden eklendi ve bu class neden order application service impl gibi package privacy değil
/*
 * bu listener (primary adapter) payment  bounded context'i tarafından gönderilen eventi takip eden
 * ve siparişin ödenme durumuna göre restaurant-service'ye event publish ederek restaurant messaging yapısının
 * dinleme yapacağı primary adapter
 * */
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        OrderPaidEvent orderPaidEvent = orderPaymentSaga.process(paymentResponse);
        log.info("Publishing OrderPaidEvent for order id: {}", paymentResponse.getOrderId());
        orderPaidEvent.fire();
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info("Order is roll backed for order id: {} with failure messages: {}",
                paymentResponse.getOrderId(),
                String.join(FAILURE_MESSAGES_DELIMITER, paymentResponse.getFailureMessages()));
    }
}
