package com.food.order.system.domain.valueobject;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

/*
 * Normalde CustomerId OrderId gibi kendi bounded context içerisinde tanımlanacaktı fakat bazı bounded context'ler için
 * bu valueobject ortak olduğu için library şeklinde tanımlanıp gerekli bounded context'lerde kullanılacak.
 * */
public class CustomerId extends BaseId<UUID> {
    protected CustomerId(UUID value) {
        super(value);
    }
}
