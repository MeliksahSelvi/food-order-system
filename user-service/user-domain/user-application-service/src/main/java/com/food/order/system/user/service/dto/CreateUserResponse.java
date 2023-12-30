package com.food.order.system.user.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class CreateUserResponse {
    @NotNull
    private final UUID userId;
    @NotNull
    private final String message;
}
