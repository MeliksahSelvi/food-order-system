package com.food.order.system.user.service.dataaccess.user.adapter;

import com.food.order.system.user.service.ports.output.EncryptPasswordPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 06.01.2024
 */

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements EncryptPasswordPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encrypt(String password) {
        return passwordEncoder.encode(password);
    }
}
