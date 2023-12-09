package com.food.order.system.domain.valueobject;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

/*
* Normalde OrderId valueobject'i order service bounded context'i içerisinde; order domain içinde Order Aggregate Root'u ve OrderItem Entity'si içinde
* kullanılmak üzere tanımlanacaktı.Fakat OrderId valueobject'i aynı zamanda Payment Service ve Restaurant service bounded context'leri içerisinde de
* kullanılacağı için ortak library içerisinde yani burada tanımlandı.
* */
public class OrderId extends BaseId<UUID> {
    public OrderId(UUID value) {
        super(value);
    }
}
