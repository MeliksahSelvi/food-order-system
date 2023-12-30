package com.food.order.system.user.service.config;

import com.food.order.system.user.service.UserDomainService;
import com.food.order.system.user.service.UserDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Configuration
public class DomainConfig {

    @Bean
    public UserDomainService userDomainService() {
        return new UserDomainServiceImpl();
    }
}
