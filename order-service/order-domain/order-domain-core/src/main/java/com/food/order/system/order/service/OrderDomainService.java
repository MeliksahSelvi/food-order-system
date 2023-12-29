package com.food.order.system.order.service;

import com.food.order.system.order.service.entity.Order;
import com.food.order.system.order.service.entity.Restaurant;
import com.food.order.system.order.service.event.OrderCancelledEvent;
import com.food.order.system.order.service.event.OrderCreatedEvent;
import com.food.order.system.order.service.event.OrderPaidEvent;

import java.util.List;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

/*
 * Bu eventlerin triggerlanması application service katmanında olacaktır.Çünkü event triggerlamadan önce
 * temel business operations'un database'e persist olmuş olması gerekiyor.Eğer event triggerlandıktan sonra
 * bir hata olursa geri alamayacağım gereksiz bir event ile karşı karşıya kalmış oluruz.Ve eğer triggerlanmayı
 * domain-core içinde yapmak istersem business logic result'u domain core içinde database persist etmem gerekiyor.
 * Fakat biz repository call işlemlerini; domain core sadece business logic'e odaklanabilmesi için application service
 * katmanında yapmayı tercih ediyoruz.Application service eventlerin ne zaman ve nasıl triggerlanacağına karar verecektir.
 * DDD'de domain service mandatory değildir. Business logic'te birden fazla aggregates'e erişim varsa veya entity'e koyulması
 * uygun olmayan business logic olduğu zaman; domain service gereklidir. Eğer domain service koymazsak application service
 * direkt olarak entity ile iletişim kuracak. Biz kişisel bir tercih yaparak application service entity'ler ile direkt iletişime
 * geçmesin diye her zaman domain service kullanıyoruz.
 * */
public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
