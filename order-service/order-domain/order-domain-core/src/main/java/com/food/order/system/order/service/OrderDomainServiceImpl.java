package com.food.order.system.order.service;

import com.food.order.system.order.service.constants.DomainConstants;
import com.food.order.system.order.service.entity.Order;
import com.food.order.system.order.service.entity.Product;
import com.food.order.system.order.service.entity.Restaurant;
import com.food.order.system.order.service.event.OrderCancelledEvent;
import com.food.order.system.order.service.event.OrderCreatedEvent;
import com.food.order.system.order.service.event.OrderPaidEvent;
import com.food.order.system.order.service.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.getActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
                    " is currently not active!");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().stream().forEach(orderItem ->
                restaurant.getProducts().stream().forEach(resturantProduct -> {
                    Product currentProduct = orderItem.getProduct();
                    if (currentProduct.equals(resturantProduct)) {
                        currentProduct.updateWithConfirmedNameAndPrice(resturantProduct.getName(), resturantProduct.getPrice());
                    }
                }));
    }
}
