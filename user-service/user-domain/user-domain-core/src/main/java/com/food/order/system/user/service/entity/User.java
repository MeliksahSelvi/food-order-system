package com.food.order.system.user.service.entity;

import com.food.order.system.user.service.common.AggregateRoot;
import com.food.order.system.user.service.valueobject.UserId;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.food.order.system.user.service.common.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

/*
 * User aggregate'sinin root'u olarak User entity'sini seçtik.
 * */
public class User extends AggregateRoot<UserId> {
    private final String email;
    private String password;
    private ZonedDateTime createdAt;

    private User(Builder builder) {
        setId(builder.userId);
        email = builder.email;
        password = builder.password;
        createdAt=builder.createdAt;
    }

    public void initializeUser() {
        setId(new UserId(UUID.randomUUID()));
        createdAt = ZonedDateTime.now(ZoneId.of(UTC));
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static final class Builder {
        private UserId userId;
        private String email;
        private String password;
        private ZonedDateTime createdAt;

        private Builder() {
        }

        public Builder userId(UserId val) {
            userId = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            createdAt = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
