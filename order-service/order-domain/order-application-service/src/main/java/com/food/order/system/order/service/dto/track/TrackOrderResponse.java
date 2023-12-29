package com.food.order.system.order.service.dto.track;

import com.food.order.system.order.service.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderResponse {
    @NotNull
    private final UUID orderTrackingId;
    @NotNull
    private final OrderStatus orderStatus;
    private final List<String> failureMessages;
}
