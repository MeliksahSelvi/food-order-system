package com.food.order.system.saga;

/**
 * @Author mselvi
 * @Created 22.12.2023
 */

public enum SagaStatus {
    STARTED,FAILED,SUCCEEDED,PROCESSING,COMPENSATING,COMPENSATED
}
