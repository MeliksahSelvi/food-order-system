package com.food.order.system.customer.service.application.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

@Data
@Builder
@AllArgsConstructor
public class ErrorDTO {
    private final String code;
    private final String message;
}
