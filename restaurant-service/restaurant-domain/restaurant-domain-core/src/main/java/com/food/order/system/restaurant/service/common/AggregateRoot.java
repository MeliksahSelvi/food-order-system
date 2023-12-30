package com.food.order.system.restaurant.service.common;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

/*
 * Tüm Aggregate'leri temsil eden marker class. Aggregate Root'lar Entity'lerin içinden seçilmelidir.
 * */
public abstract class AggregateRoot<ID> extends BaseEntity<ID> {
}
