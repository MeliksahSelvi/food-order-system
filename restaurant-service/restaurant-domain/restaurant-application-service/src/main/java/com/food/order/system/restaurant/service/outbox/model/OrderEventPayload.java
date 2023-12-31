package com.food.order.system.restaurant.service.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class OrderEventPayload {
    @JsonProperty
    private String restaurantId;
    @JsonProperty
    private String orderId;
    @JsonProperty
    private ZonedDateTime createdAt;
    @JsonProperty
    private String orderApprovalStatus;
    @JsonProperty
    private List<String> failureMessages;
}
