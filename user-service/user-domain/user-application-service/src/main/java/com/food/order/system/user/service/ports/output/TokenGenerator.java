package com.food.order.system.user.service.ports.output;

import com.food.order.system.user.service.entity.User;
import com.food.order.system.user.service.dto.JwtToken;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

/*
* Token üretmek için output port
* */
public interface TokenGenerator {

    JwtToken generateToken(User user);
}
