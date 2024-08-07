package com.wanted.august.model.entity;

import com.wanted.august.model.UserRole;

import javax.persistence.*;

import com.wanted.august.model.request.UserJoinRequest;
import lombok.*;
import org.hibernate.annotations.Where;

@Data
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

    private String nickName; // 작성자명 대체

    private String password;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    public static UserEntity toEntity(UserJoinRequest request, String encodedPassword) {
        return UserEntity.builder()
                .userName(request.getUserName())
                .nickName(request.getNickName())
                .password(encodedPassword)
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(request.getRole())
                .build();
    }
}
