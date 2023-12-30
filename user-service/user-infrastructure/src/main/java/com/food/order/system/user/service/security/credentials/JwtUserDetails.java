package com.food.order.system.user.service.security.credentials;

import com.food.order.system.user.service.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

public class JwtUserDetails implements UserDetails {

    private UUID id;
    private String email;
    private String password;

    public JwtUserDetails(User user) {
        this.id = user.getId().getValue();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UUID getId() {
        return id;
    }
}
