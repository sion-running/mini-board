package com.wanted.august.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wanted.august.model.UserRole;
import com.wanted.august.model.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String password;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
}
