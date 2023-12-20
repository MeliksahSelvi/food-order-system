package com.food.order.system.restaurant.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Data
@Configuration
@ConfigurationProperties("restaurant-service")
public class RestaurantServiceConfigData {
    private String restaurantApprovalRequestTopicName;
    private String restaurantApprovalResponseTopicName;
}
