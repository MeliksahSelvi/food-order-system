package com.food.order.system.user.service;

import com.food.order.system.user.service.dto.CreateUserCommand;
import com.food.order.system.user.service.dto.CreateUserResponse;
import com.food.order.system.user.service.entity.User;
import com.food.order.system.user.service.exception.UserDomainException;
import com.food.order.system.user.service.ports.output.EncryptPasswordPort;
import com.food.order.system.user.service.ports.output.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCreateCommandHandler {

    private final UserDomainService userDomainService;
    private final EncryptPasswordPort encryptPasswordPort;
    private final UserRepository userRepository;

    @Transactional
    public CreateUserResponse persistUser(CreateUserCommand createUserCommand) {
        User user = createUserCommand.toModel();
        userDomainService.validateAndInitiate(user);
        String encryptedPassword = encryptPasswordPort.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);
        User savedUser = userRepository.createUser(user);
        if (savedUser == null) {
            log.error("Could not save user with id: {}", createUserCommand.getUserId());
            throw new UserDomainException("Could not save user with id: " +
                    createUserCommand.getUserId());
        }
        return new CreateUserResponse(savedUser.getId().getValue(), "User Saved Successfully!");
    }
}
