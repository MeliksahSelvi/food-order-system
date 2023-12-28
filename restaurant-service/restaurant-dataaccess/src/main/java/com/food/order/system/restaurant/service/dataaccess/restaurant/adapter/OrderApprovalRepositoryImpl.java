package com.food.order.system.restaurant.service.dataaccess.restaurant.adapter;

import com.food.order.system.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity;
import com.food.order.system.restaurant.service.dataaccess.restaurant.repository.OrderApprovalJpaRepository;
import com.food.order.system.restaurant.service.domain.entity.OrderApproval;
import com.food.order.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * OrderApproval entity'sinin secondary adapter'ı
 * */
@Component
@RequiredArgsConstructor
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        OrderApprovalEntity orderApprovalEntity = OrderApprovalEntity.builder()
                .id(orderApproval.getId().getValue())
                .restaurantId(orderApproval.getRestaurantId().getValue())
                .orderId(orderApproval.getOrderId().getValue())
                .status(orderApproval.getApprovalStatus())
                .build();
        return orderApprovalJpaRepository.save(orderApprovalEntity).toModel();
    }
}
