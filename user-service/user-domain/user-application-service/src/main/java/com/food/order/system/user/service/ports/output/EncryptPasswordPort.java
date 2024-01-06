package com.food.order.system.user.service.ports.output;

/**
 * @Author mselvi
 * @Created 06.01.2024
 */

public interface EncryptPasswordPort {

    String encrypt(String password);
}
