package com.food.order.system.customer.service.domain.create;

import com.food.order.system.customer.service.domain.entity.Customer;
import com.food.order.system.customer.service.domain.valueobject.CustomerId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class CreateCustomerCommand {
    @NotNull
    private final UUID customerId;
    @NotNull
    private final String username;
    @NotNull
    private final String firstName;
    @NotNull
    private final String lastName;

    public Customer toModel() {
        return Customer.builder()
                .customerId(new CustomerId(customerId))
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
