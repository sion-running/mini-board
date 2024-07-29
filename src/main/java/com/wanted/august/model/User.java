package com.wanted.august.model;

import javax.persistence.*;

public class User {

    private Long id;

    private String userName;

    private String writer;

    private String password;

    private String email;

    private String phone;

    private UserRole role;
}
