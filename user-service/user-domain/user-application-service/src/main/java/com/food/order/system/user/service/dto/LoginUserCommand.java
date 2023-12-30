package com.food.order.system.user.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class LoginUserCommand {
    @NotNull
    @Email(message = "Invalid email. Please enter a valid email address")
    private final String email;
    @NotNull
    private final String password;
}
