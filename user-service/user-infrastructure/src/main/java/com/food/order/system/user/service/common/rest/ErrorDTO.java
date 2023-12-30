package com.food.order.system.user.service.common.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Data
@Builder
@AllArgsConstructor
public class ErrorDTO {
    private final String code;
    private final String message;
}
