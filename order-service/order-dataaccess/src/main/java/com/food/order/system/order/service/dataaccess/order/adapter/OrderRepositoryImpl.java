package com.food.order.system.order.service.dataaccess.order.adapter;

import com.food.order.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.order.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.food.order.system.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.order.system.order.service.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

/*
* Order aggregate root'unun secondary adapter'ı
* */
@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderDataAccessMapper.orderToOrderEntity(order);
        OrderEntity savedOrderEntity = orderJpaRepository.save(orderEntity);
        return orderDataAccessMapper.orderEntityToOrder(savedOrderEntity);
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        Optional<OrderEntity> orderEntityOptional = orderJpaRepository.findByTrackingId(trackingId.getValue());
        return orderEntityOptional.map(orderDataAccessMapper::orderEntityToOrder);
    }
}
