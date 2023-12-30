package com.food.order.system.user.service.dataaccess.user.entity;

import com.food.order.system.user.service.entity.User;
import com.food.order.system.user.service.valueobject.UserId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;
    private String email;
    private String password;
    private ZonedDateTime createdAt;

    public User toModel(){
        return User.builder()
                .userId(new UserId(id))
                .email(email)
                .password(password)
                .createdAt(createdAt)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
