package com.food.order.system.payment.service.common;

import java.util.Objects;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

/*
 * Tüm entity'leri temsil eden base class
 * */
public abstract class BaseEntity<ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) obj;
        return id.equals(that.id);
    }
}
