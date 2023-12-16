package com.food.order.system.order.service.domain;

import com.food.order.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.order.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.exception.OrderNotFoundException;
import com.food.order.system.order.service.domain.mapper.OrderDataMapper;
import com.food.order.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.order.system.order.service.domain.valueobject.TrackingId;
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
    private final OrderDataMapper orderDataMapper;

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery){

        Optional<Order> orderOptional = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
        if (orderOptional.isEmpty()) {
            log.warn("Could not find order with tracking id: {}",trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id: "+trackOrderQuery.getOrderTrackingId());
        }
        return orderDataMapper.orderToTrackOrderResponse(orderOptional.get());
    }
}
