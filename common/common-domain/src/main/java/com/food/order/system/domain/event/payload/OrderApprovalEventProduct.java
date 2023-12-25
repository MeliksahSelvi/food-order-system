package com.food.order.system.domain.event.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

/*
 * payment outbox tablosundaki payload kolonunun alt kolonu
 * */
@Getter
@Builder
@AllArgsConstructor
public class OrderApprovalEventProduct {
    @JsonProperty
    private String id;
    @JsonProperty
    private Integer quantity;
}
