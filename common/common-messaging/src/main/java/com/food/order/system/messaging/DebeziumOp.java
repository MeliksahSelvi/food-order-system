package com.food.order.system.messaging;

/**
 * @Author mselvi
 * @Created 25.12.2023
 */

public enum DebeziumOp {

    CREATE("c"),UPDATE("u"),DELETE("d");

    private final String value;

    DebeziumOp(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
