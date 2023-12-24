package com.food.order.system.customer.service;

import com.food.order.system.customer.service.domain.CustomerDomainService;
import com.food.order.system.customer.service.domain.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
