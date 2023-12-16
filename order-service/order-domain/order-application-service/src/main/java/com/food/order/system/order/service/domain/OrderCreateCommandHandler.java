package com.food.order.system.order.service.domain;

import com.food.order.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.order.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.order.system.order.service.domain.event.OrderCreatedEvent;
import com.food.order.system.order.service.domain.mapper.OrderDataMapper;
import com.food.order.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        /*
        * varsayılan spring aop ayarlarını kullandığımız için transactional gibi mekanizmaların çalışması için ilgili methodun
        * proxy'sisi üzerinde işlem yapılması gerekir yani başka bir method tarafından invoke edilmesi gerekir.
        * Ayrıca ilgili methodun public olması gerekir.Bu sebeple persistOrder methodu OrderCreateHelper adından başka bir component üzerinde tanımlandı.
        * Bu kısıtlamalar ile sınırlanmak istemiyorsak AspectJ kullanabiliriz.
        * */
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(),"Order Created Successfully");
    }


}
