package com.food.order.system.payment.service.config;

import com.food.order.system.payment.service.PaymentDomainService;
import com.food.order.system.payment.service.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

@Configuration
public class DomainConfig {

    /*
     * PaymentDomainServiceImpl domain-core içinde olduğu için herhangi bir sprean işaretlemesi yapmadık fakat
     * uygulamada bean olarak işaretlenmesi gerektiği için onu burada configuration ayarlarında application context'e ekliyoruz.
     * */
    @Bean
    public PaymentDomainService paymentDomainService(){
        return new PaymentDomainServiceImpl();
    }
}
