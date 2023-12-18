package com.food.order.system.order.service.domain;

import com.food.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.order.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Service
@Slf4j
@Validated//neden eklendi ve bu class neden order application service impl gibi package privacy değil
/*
 * bu listener (primary adapter) restaurant  bounded context'i tarafından gönderilen eventi takip eden messaging yapısının
 * çağıracağı input portunun implementation'u
 * */
public class RestaurantApprovedResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {
    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
