package com.food.order.system.user.service.ports.input.service;

import com.food.order.system.user.service.dto.CreateUserCommand;
import com.food.order.system.user.service.dto.CreateUserResponse;
import com.food.order.system.user.service.dto.JwtToken;
import com.food.order.system.user.service.dto.LoginUserCommand;
import jakarta.validation.Valid;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

public interface UserApplicationService {

    CreateUserResponse createUser(@Valid CreateUserCommand createUserCommand);

    JwtToken loginUser(@Valid LoginUserCommand loginUserCommand);
}
