package com.food.order.system.order.service.dto.create;

import com.food.order.system.order.service.valueobject.StreetAddress;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class OrderAddress {
    @NotNull
    @Max(value = 50)
    private final String street;
    @NotNull
    @Max(value = 10)
    private final String postalCode;
    @NotNull
    @Max(value = 50)
    private final String city;

    public StreetAddress toObject() {
        return new StreetAddress(UUID.randomUUID(), street, postalCode, city);
    }
}
