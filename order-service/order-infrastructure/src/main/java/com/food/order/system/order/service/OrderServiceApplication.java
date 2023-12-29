package com.food.order.system.order.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */


@EnableJpaRepositories(basePackages = {"com.food.order.system.order.service.dataaccess"})
@EntityScan(basePackages = {"com.food.order.system.order.service.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.food.order.system.order.service")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
