package com.food.order.system.order.service.ports.output.repository;

import com.food.order.system.order.service.entity.Order;
import com.food.order.system.order.service.valueobject.OrderId;
import com.food.order.system.order.service.valueobject.TrackingId;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Order Aggregate Root'unun output portu
 * */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId orderId);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}
