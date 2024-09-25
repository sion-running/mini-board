package com.wanted.august.service;

import com.wanted.august.model.Token;
import com.wanted.august.model.User;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.UserJoinRequest;
import com.wanted.august.model.request.UserLoginRequest;

public interface UserService {
    User join(UserJoinRequest request);

    Token login(UserLoginRequest request);

    User loadUserByUsername(String username);

    UserEntity findByUserNameOrElseThrow(String userName);
}
