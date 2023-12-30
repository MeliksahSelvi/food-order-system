package com.food.order.system.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @Author mselvi
 * @Created 26.12.2023
 */

@EnableConfigServer
@SpringBootApplication(scanBasePackages = "com.food.order.system.config.server")
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class,args);
    }
}
