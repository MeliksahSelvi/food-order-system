package com.food.order.system.order.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class CustomerModel {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private ZonedDateTime createdAt;
}
