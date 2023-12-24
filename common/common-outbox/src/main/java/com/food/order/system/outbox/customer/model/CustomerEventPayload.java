package com.food.order.system.outbox.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * customer outbox tablosundaki payload kolonu
 * */
@Getter
@Builder
@AllArgsConstructor
public class CustomerEventPayload {
    @JsonProperty
    private String customerId;
    @JsonProperty
    private String username;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private ZonedDateTime createdAt;
}
