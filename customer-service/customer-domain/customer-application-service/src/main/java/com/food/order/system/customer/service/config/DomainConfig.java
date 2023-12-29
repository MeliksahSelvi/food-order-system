package com.food.order.system.customer.service.config;

import com.food.order.system.customer.service.CustomerDomainService;
import com.food.order.system.customer.service.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

@Configuration
public class DomainConfig {

    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
