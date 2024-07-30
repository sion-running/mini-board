package com.wanted.august.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanted.august.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
public class User implements UserDetails {

    private Long id;
    private String username;
    private String writer;
    private String password;
    private String email;
    private String phone;
    private UserRole role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static User fromEntity(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUserName())
                .writer(userEntity.getNickName())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .build();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return getDeletedAt() == null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return getDeletedAt() == null;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return getDeletedAt() == null;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return getDeletedAt() == null;
    }
}
