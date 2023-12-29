package com.food.order.system.order.service.config;

import com.food.order.system.order.service.OrderDomainService;
import com.food.order.system.order.service.OrderDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

@Configuration
public class DomainConfig {

    @Bean
    public OrderDomainService customerDomainService() {
        return new OrderDomainServiceImpl();
    }
}
