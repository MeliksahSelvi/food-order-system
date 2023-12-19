package com.food.order.system.order.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 19.12.2023
 */

@Configuration
public class BeanConfiguration {

    /*
    * OrderDomainServiceImpl domain-core içinde olduğu için herhangi bir sprean işaretlemesi yapmadık fakat
    * uygulamada bean olarak işaretlenmesi gerektiği için onu burada configuration ayarlarında application context'e ekliyoruz.
    * */
    @Bean
    public OrderDomainService orderDomainService(){
        return new OrderDomainServiceImpl();
    }
}
