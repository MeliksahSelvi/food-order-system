package com.food.order.system.domain.event.publisher;

import com.food.order.system.domain.event.DomainEvent;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
