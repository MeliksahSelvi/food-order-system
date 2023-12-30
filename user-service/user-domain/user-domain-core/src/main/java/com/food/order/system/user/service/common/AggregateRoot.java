package com.food.order.system.user.service.common;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

/*
 * Tüm Aggregate'leri temsil eden marker class. Aggregate Root'lar Entity'lerin içinden seçilmelidir.
 * */
public abstract class AggregateRoot<ID> extends BaseEntity<ID> {
}
