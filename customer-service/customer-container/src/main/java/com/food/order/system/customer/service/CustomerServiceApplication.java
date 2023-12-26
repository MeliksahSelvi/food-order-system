package com.food.order.system.customer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

@EnableJpaRepositories(basePackages = {"com.food.order.system.customer.service.dataaccess","com.food.order.system.dataaccess.customer"})
@EntityScan(basePackages = {"com.food.order.system.customer.service.dataaccess","com.food.order.system.dataaccess.customer"})
@SpringBootApplication(scanBasePackages = "com.food.order.system")
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
