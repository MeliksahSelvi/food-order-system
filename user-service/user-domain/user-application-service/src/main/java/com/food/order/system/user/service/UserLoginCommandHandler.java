package com.food.order.system.user.service;

import com.food.order.system.user.service.dto.LoginUserCommand;
import com.food.order.system.user.service.entity.User;
import com.food.order.system.user.service.exception.UserNotFoundException;
import com.food.order.system.user.service.ports.output.TokenGenerator;
import com.food.order.system.user.service.ports.output.UserRepository;
import com.food.order.system.user.service.dto.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class UserLoginCommandHandler {

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public JwtToken generateJwtToken(LoginUserCommand loginUserCommand) {
        String email = loginUserCommand.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            log.warn("Could not find user with email: {}", email);
            throw new UserNotFoundException("Could not find user with email: " + email);
        }
        JwtToken jwtToken = tokenGenerator.generateToken(userOptional.get());
        log.info("Token generated with email: {}", email);
        return jwtToken;
    }
}
