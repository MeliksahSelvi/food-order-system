package com.food.order.system.saga;

import com.food.order.system.domain.event.DomainEvent;

/**
 * @Author mselvi
 * @Created 21.12.2023
 */

public interface SagaStep<T> {
    void process(T data);
    void rollback(T data);
}
