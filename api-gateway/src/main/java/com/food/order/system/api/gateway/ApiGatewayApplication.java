package com.food.order.system.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@SpringBootApplication(scanBasePackages = "com.food.order.system.api.gateway")
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
