package com.food.order.system.order.service;

import com.food.order.system.order.service.dto.track.TrackOrderQuery;
import com.food.order.system.order.service.dto.track.TrackOrderResponse;
import com.food.order.system.order.service.entity.Order;
import com.food.order.system.order.service.exception.OrderNotFoundException;
import com.food.order.system.order.service.ports.output.repository.OrderRepository;
import com.food.order.system.order.service.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {

        Optional<Order> orderOptional = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
        if (orderOptional.isEmpty()) {
            log.warn("Could not find order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id: " + trackOrderQuery.getOrderTrackingId());
        }
        return createTrackOrderResponse(orderOptional.get());
    }

    private TrackOrderResponse createTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }
}
