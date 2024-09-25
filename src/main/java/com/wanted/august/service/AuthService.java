package com.wanted.august.service;

import com.wanted.august.model.Token;

public interface AuthService {
    String generateRefreshToken(String userName);
    Token refresh(String userName, String refreshToken);
}
