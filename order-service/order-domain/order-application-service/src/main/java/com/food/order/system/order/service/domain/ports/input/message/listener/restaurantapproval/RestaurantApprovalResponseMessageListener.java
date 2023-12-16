package com.food.order.system.order.service.domain.ports.input.message.listener.restaurantapproval;

import com.food.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
 * Restaurant üzerinde approval işlemi yapıldığında oluşan sonucu dinleyen input portu.Domain event listener'lar application service katmanında olur.
 * Aynı zamanda bu event listener'lar domain event'ler tarafından tetiklenirler.
 * */
public interface RestaurantApprovalResponseMessageListener {

    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
