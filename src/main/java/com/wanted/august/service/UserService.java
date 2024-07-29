package com.wanted.august.service;

import com.wanted.august.model.User;
import com.wanted.august.model.request.UserJoinRequest;
import com.wanted.august.model.request.UserLoginRequest;

public interface UserService {
    User join(UserJoinRequest request);

    String login(UserLoginRequest request);

    User loadUserByUsername(String username);
}
