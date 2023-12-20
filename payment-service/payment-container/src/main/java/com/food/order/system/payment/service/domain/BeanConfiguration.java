package com.food.order.system.payment.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Configuration
public class BeanConfiguration {

    /*
     * PaymentDomainServiceImpl domain-core içinde olduğu için herhangi bir sprean işaretlemesi yapmadık fakat
     * uygulamada bean olarak işaretlenmesi gerektiği için onu burada configuration ayarlarında application context'e ekliyoruz.
     * */
    @Bean
    public PaymentDomainService paymentDomainService(){
        return new PaymentDomainServiceImpl();
    }
}
