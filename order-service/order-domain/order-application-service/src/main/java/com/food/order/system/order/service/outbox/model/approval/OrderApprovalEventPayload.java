package com.food.order.system.order.service.outbox.model.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

/*
 * restaurant_approval_outbox tablosunun payload kolonu
 * */
@Getter
@Builder
@AllArgsConstructor
public class OrderApprovalEventPayload {
    @JsonProperty
    private String orderId;
    @JsonProperty
    private String restaurantId;
    @JsonProperty
    private BigDecimal price;
    @JsonProperty
    private ZonedDateTime createdAt;
    @JsonProperty
    private String restaurantOrderStatus;
    @JsonProperty
    private List<OrderApprovalEventProduct> products;
}
