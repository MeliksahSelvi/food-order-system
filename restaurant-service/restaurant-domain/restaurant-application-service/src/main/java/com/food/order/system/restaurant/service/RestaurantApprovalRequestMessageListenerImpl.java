package com.food.order.system.restaurant.service;

import com.food.order.system.restaurant.service.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.service.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
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
        restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);
    }
}
