package com.food.order.system.domain.valueobject;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

/*
 * Normalde RestaurantId OrderId gibi kendi bounded context içerisinde tanımlanacaktı fakat bazı bounded context'ler için
 * bu valueobject ortak olduğu için library şeklinde tanımlanıp gerekli bounded context'lerde kullanılacak.
 * */
public class RestaurantId extends BaseId<UUID> {
    protected RestaurantId(UUID value) {
        super(value);
    }
}
