package com.food.order.system.order.service.ports.input.message.listener.restaurantapproval;

import com.food.order.system.order.service.dto.message.RestaurantApprovalResponse;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Restaurant üzerinde approval işlemi yapıldığında oluşan sonucu dinleyen messaging yapısının çağıracağı input portu.
 * Implementation'u (primary adapter) yine application-service'de olacak.
 * */
public interface RestaurantApprovalResponseMessageListener {

    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
