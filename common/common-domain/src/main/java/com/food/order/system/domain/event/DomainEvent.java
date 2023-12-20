package com.food.order.system.domain.event;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

/*
* Domain bazlı eventler için marker interface. Generic aldığı tür entity'i temsil edecek.
* */
public interface DomainEvent<T>{
    /*
    * Bu method application service üzerinde domain eventleri kolayca fırlatabilmek için eklendi.
    * */
    void fire();
}
