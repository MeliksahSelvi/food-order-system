package com.food.order.system.saga;

import com.food.order.system.domain.event.DomainEvent;

/**
 * @Author mselvi
 * @Created 21.12.2023
 */

public interface SagaStep<T, S extends DomainEvent, U extends DomainEvent> {
    S process(T data);
    U rollback(T data);
}
