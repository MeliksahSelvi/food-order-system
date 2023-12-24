package com.food.order.system.order.service.domain.ports.input.message.listener.customer;

import com.food.order.system.order.service.domain.dto.message.CustomerModel;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
* customer service bounded context'inin publish ettiği customercreated eventi'nin triggerlardığı kafka listenerı kullanan input portu
* */
public interface CustomerMessageListener {

    void customerCreated(CustomerModel customerModel);
}
