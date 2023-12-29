package com.food.order.system.order.service.saga;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

public interface SagaStep<T> {
    void process(T data);

    void rollback(T data);
}
