package com.wanted.august.model;

import com.wanted.august.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Builder
public class User {

    private Long id;

    private String userName;

    private String writer;

    private String password;

    private String email;

    private String phone;

    private UserRole role;

    public static User fromEntity(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .writer(userEntity.getWriter())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .build();
    }
}
