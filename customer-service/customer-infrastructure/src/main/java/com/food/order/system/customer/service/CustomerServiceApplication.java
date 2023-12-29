package com.food.order.system.customer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

@EnableJpaRepositories(basePackages = {"com.food.order.system.customer.service.dataaccess"})
@EntityScan(basePackages = {"com.food.order.system.customer.service.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.food.order.system.customer.service")
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
