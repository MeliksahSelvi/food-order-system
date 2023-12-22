package com.food.order.system.outbox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

/*
* Tek görevi outbox modulünü kullananlar için scheduler'i enable etmek
* */
@Configuration
@EnableScheduling
public class SchedulerConfig {
}
