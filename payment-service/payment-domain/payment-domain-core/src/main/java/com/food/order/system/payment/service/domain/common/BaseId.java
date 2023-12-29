package com.food.order.system.payment.service.domain.common;

import java.util.Objects;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

/*
 * Tüm bounded context'lerin içindeki Id değeri olan valueobject'leri temsil eden base class.
 * */
public abstract class BaseId<T> {
    private final T value;

    protected BaseId(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return value.equals(baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
