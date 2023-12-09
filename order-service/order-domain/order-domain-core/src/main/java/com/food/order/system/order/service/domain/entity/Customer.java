package com.food.order.system.order.service.domain.entity;

import com.food.order.system.domain.entity.AggregateRoot;
import com.food.order.system.domain.valueobject.CustomerId;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

public class Customer extends AggregateRoot<CustomerId> {

    //todo burayı eklemedi neden? Customer oluştururken CustomerId değerini nasıl veriyor bakalım.
    public Customer(CustomerId customerId) {
        setId(customerId);
    }
}
