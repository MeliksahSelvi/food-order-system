package com.food.order.system.order.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "order-service")
public class OrderServiceConfigData {
    private String paymentRequestTopicName;
    private String paymentResponseTopicName;
    private String restaurantApprovalRequestTopicName;
    private String restaurantApprovalResponseTopicName;
}
