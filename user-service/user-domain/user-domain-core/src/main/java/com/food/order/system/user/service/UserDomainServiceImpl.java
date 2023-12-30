package com.food.order.system.user.service;

import com.food.order.system.user.service.entity.User;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Slf4j
public class UserDomainServiceImpl implements UserDomainService {

    @Override
    public void validateAndInitiate(User user) {
        user.initializeUser();
        log.info("User with id: {} is initiated", user.getId().getValue());
    }
}
