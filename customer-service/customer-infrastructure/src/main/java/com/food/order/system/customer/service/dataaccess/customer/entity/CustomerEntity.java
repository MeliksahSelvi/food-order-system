package com.food.order.system.customer.service.dataaccess.customer.entity;

import com.food.order.system.customer.service.entity.Customer;
import com.food.order.system.customer.service.valueobject.CustomerId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class CustomerEntity {

    @Id
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;

    public Customer toModel(){
        return Customer.builder()
                .customerId(new CustomerId(id))
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
