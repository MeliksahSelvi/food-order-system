package com.food.order.system.order.service;

import com.food.order.system.order.service.entity.Order;
import com.food.order.system.order.service.exception.OrderNotFoundException;
import com.food.order.system.order.service.ports.output.repository.OrderRepository;
import com.food.order.system.order.service.saga.SagaStatus;
import com.food.order.system.order.service.valueobject.OrderId;
import com.food.order.system.order.service.valueobject.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 21.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaHelper {

    private final OrderRepository orderRepository;

    Order findOrder(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(new OrderId(UUID.fromString(orderId)));
        if (orderOptional.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id: " + orderId + " could not be found!");
        }
        return orderOptional.get();
    }

    void saveOrder(Order order) {
        orderRepository.save(order);
    }

    SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
        return switch (orderStatus) {
            case PENDING -> SagaStatus.STARTED;
            case PAID -> SagaStatus.PROCESSING;
            case APPROVED -> SagaStatus.SUCCEEDED;
            case CANCELLING -> SagaStatus.COMPENSATING;
            case CANCELLED -> SagaStatus.COMPENSATED;
        };
    }
}
