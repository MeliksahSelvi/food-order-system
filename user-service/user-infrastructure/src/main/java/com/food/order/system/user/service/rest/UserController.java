package com.food.order.system.user.service.rest;

import com.food.order.system.user.service.dto.CreateUserCommand;
import com.food.order.system.user.service.dto.CreateUserResponse;
import com.food.order.system.user.service.dto.JwtToken;
import com.food.order.system.user.service.dto.LoginUserCommand;
import com.food.order.system.user.service.ports.input.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth", produces = "application/vnd.api.v1+json")
public class UserController {

    private final UserApplicationService userApplicationService;

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserCommand createUserCommand) {
        log.info("Creating user with email: {}", createUserCommand.getEmail());
        CreateUserResponse createUserResponse = userApplicationService.createUser(createUserCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> loginUser(@RequestBody LoginUserCommand loginUserCommand) {
        log.info("Produce token with email: {}", loginUserCommand.getEmail());
        JwtToken jwtToken = userApplicationService.loginUser(loginUserCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
    }
}
