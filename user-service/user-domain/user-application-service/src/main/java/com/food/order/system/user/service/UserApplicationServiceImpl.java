package com.food.order.system.user.service;

import com.food.order.system.user.service.dto.CreateUserCommand;
import com.food.order.system.user.service.dto.CreateUserResponse;
import com.food.order.system.user.service.dto.JwtToken;
import com.food.order.system.user.service.dto.LoginUserCommand;
import com.food.order.system.user.service.ports.input.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Slf4j
@Validated
@Component
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

    private final UserCreateCommandHandler userCreateCommandHandler;
    private final UserLoginCommandHandler userLoginCommandHandler;

    @Override
    public CreateUserResponse createUser(CreateUserCommand createUserCommand) {
        return userCreateCommandHandler.persistUser(createUserCommand);
    }

    @Override
    public JwtToken loginUser(LoginUserCommand loginUserCommand) {
        return userLoginCommandHandler.generateJwtToken(loginUserCommand);
    }
}
