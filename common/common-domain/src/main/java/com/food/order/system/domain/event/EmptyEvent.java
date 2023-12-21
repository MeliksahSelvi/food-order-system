package com.food.order.system.domain.event;

/**
 * @Author mselvi
 * @Created 21.12.2023
 */

public final class EmptyEvent implements DomainEvent<Void> {

    public static final EmptyEvent INSTANCE = new EmptyEvent();

    private EmptyEvent() {
    }

    @Override
    public void fire() {

    }
}
