package com.food.order.system.user.service.dto;

import com.food.order.system.user.service.entity.User;
import com.food.order.system.user.service.valueobject.UserId;
import jakarta.validation.constraints.Email;
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
public class CreateUserCommand {
    @NotNull
    private final UUID userId;
    @NotNull
    @Email(message = "Invalid email. Please enter a valid email address")
    private final String email;
    @NotNull
    private final String password;

    public User toModel() {
        return User.builder()
                .userId(new UserId(userId))
                .email(email)
                .password(password)
                .build();
    }
}
