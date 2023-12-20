package com.food.order.system.payment.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "payment-service")
public class PaymentServiceConfigData {
    private String paymentRequestTopicName;
    private String paymentResponseTopicName;
}
