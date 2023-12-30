package com.food.order.system.discovery.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@EnableEurekaServer
@SpringBootApplication(scanBasePackages = "com.food.order.system.discovery.service")
public class DiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class);
    }
}
