package com.food.order.system.user.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Data
@Builder
@AllArgsConstructor
public class JwtToken {

    private UUID userId;
    private String token;
    private Long tokenIssuedTime;
}
