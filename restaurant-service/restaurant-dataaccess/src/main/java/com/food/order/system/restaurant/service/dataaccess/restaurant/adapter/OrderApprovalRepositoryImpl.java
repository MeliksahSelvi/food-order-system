package com.food.order.system.restaurant.service.dataaccess.restaurant.adapter;

import com.food.order.system.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity;
import com.food.order.system.restaurant.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
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
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        OrderApprovalEntity orderApprovalEntity = restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval);
        orderApprovalEntity = orderApprovalJpaRepository.save(orderApprovalEntity);
        return restaurantDataAccessMapper.orderApprovalEntityToOrderApproval(orderApprovalEntity);
    }
}
