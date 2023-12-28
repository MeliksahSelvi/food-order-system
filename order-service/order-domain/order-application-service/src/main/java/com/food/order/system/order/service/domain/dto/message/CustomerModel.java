package com.food.order.system.order.service.domain.dto.message;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.order.service.domain.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Getter
@Builder
@AllArgsConstructor
public class CustomerModel {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private ZonedDateTime createdAt;

    public Customer toModel() {
        return Customer.builder()
                .customerId(new CustomerId(UUID.fromString(id)))
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
