package com.food.order.system.restaurant.service.domain.config;

import com.food.order.system.restaurant.service.domain.RestaurantDomainService;
import com.food.order.system.restaurant.service.domain.RestaurantDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

@Configuration
public class DomainConfig {

    /*
     * RestaurantDomainServiceImpl domain-core içinde olduğu için herhangi bir sprean işaretlemesi yapmadık fakat
     * uygulamada bean olarak işaretlenmesi gerektiği için onu burada configuration ayarlarında application context'e ekliyoruz.
     * */
    @Bean
    public RestaurantDomainService restaurantDomainService(){
        return new RestaurantDomainServiceImpl();
    }
}
