package com.food.order.system.order.service.dto.create;

import com.food.order.system.order.service.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {
    @NotNull
    private final UUID orderTrackingId;
    @NotNull
    private final OrderStatus orderStatus;
    @NotNull
    private final String message;
}
