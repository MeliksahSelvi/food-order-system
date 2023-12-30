package com.food.order.system.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@EnableJpaRepositories(basePackages = {"com.food.order.system.user.service.dataaccess"})
@EntityScan(basePackages = {"com.food.order.system.customer.service.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.food.order.system.user.service")
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
