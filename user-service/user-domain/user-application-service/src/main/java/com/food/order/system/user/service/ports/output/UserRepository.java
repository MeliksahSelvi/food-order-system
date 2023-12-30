package com.food.order.system.user.service.ports.output;

import com.food.order.system.user.service.entity.User;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

/*
 * User aggregate root'unun output portu
 * */
public interface UserRepository {

    User createUser(User user);

    Optional<User> findByEmail(String email);
}
