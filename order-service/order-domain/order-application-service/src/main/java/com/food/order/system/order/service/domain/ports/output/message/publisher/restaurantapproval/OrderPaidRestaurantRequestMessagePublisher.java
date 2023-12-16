package com.food.order.system.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.order.service.domain.event.OrderPaidEvent;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Bir order işleminde ödeme yapıldığında domain core layer, bu output portu kullanarak OrderPaidEvent publish edecek.
 * */
public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
