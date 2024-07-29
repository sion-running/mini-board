package com.wanted.august.model.entity;

import com.wanted.august.model.UserRole;

import javax.persistence.*;

import com.wanted.august.model.request.UserJoinRequest;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Builder
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true)
    private String userName;

    private String writer;

    private String password;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    public static UserEntity toEntity(UserJoinRequest request) {
        return UserEntity.builder()
                .userName(request.getUserName())
                .writer(request.getWriter())
                .password(request.getPassword())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }
}
