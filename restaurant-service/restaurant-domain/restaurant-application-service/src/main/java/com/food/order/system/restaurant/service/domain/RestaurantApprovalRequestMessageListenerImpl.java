package com.food.order.system.restaurant.service.domain;

import com.food.order.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.order.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

/*
 * Bu listener order bounded context'i tarafından gönderilen eventi takip eden restaurant messaging yapısının
 * çağıracağı input portunun primary adapter'i
 * */
@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {

    private final RestaurantApprovalRequestHelper restaurantApprovalRequestHelper;

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        OrderApprovalEvent orderApprovalEvent = restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);
        orderApprovalEvent.fire();
    }
}
