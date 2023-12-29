package com.food.order.system.order.service.saga;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

public enum SagaStatus {
    STARTED,FAILED,SUCCEEDED,PROCESSING,COMPENSATING,COMPENSATED
}
