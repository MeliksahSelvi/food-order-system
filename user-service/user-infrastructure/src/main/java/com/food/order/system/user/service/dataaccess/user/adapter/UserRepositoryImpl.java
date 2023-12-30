package com.food.order.system.user.service.dataaccess.user.adapter;

import com.food.order.system.user.service.dataaccess.user.entity.UserEntity;
import com.food.order.system.user.service.dataaccess.user.repository.UserJpaRepository;
import com.food.order.system.user.service.entity.User;
import com.food.order.system.user.service.ports.output.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

/*
 * User aggregate root'unun secondary adapterı
 * */
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User createUser(User user) {
        UserEntity userEntity=UserEntity.builder()
                .id(user.getId().getValue())
                .email(user.getEmail())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .build();

        return userJpaRepository.save(userEntity).toModel();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity = userJpaRepository.findByEmail(email);
        return userEntity.map(UserEntity::toModel);
    }
}
