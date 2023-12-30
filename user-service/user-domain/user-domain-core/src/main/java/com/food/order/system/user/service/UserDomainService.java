package com.food.order.system.user.service;

import com.food.order.system.user.service.entity.User;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

public interface UserDomainService {

    void validateAndInitiate(User user);
}
