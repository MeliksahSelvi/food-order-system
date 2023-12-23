package com.food.order.system.order.service.domain;

import com.food.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.order.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.food.order.system.order.service.domain.entity.Order.FAILURE_MESSAGES_DELIMITER;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Service
@Slf4j
@RequiredArgsConstructor
/*
* bu listener (primary adapter) restaurant  bounded context'i tarafından gönderilen eventi takip eden messaging yapısının
* çağıracağı input portunun implementation'u
* */
class RestaurantApprovedResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    private final OrderApprovalSaga orderApprovalSaga;

    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {
        orderApprovalSaga.process(restaurantApprovalResponse);
        log.info("Order is approved for order id: {}", restaurantApprovalResponse.getOrderId());
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {
        orderApprovalSaga.rollback(restaurantApprovalResponse);
        log.info("Order Approval Saga rollback operation is completed for order id: {} with failure messages: {}",
                restaurantApprovalResponse.getOrderId(),
                String.join(FAILURE_MESSAGES_DELIMITER, restaurantApprovalResponse.getFailureMessages()));
    }
}
