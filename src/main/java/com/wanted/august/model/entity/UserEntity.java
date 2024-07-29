package com.wanted.august.model.entity;

import com.wanted.august.model.UserRole;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String writer;

    private String password;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
}
