package com.food.order.system.saga;

/**
 * @Author mselvi
 * @Created 21.12.2023
 */

public interface SagaStep<T> {
    void process(T data);

    void rollback(T data);
}
