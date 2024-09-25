package com.wanted.august.controller;

import com.wanted.august.model.Token;
import com.wanted.august.model.response.Response;
import com.wanted.august.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // Access Token 재발급 (Refresh Token 사용)
    @PostMapping("/token/refresh")
    public Response<Token> refreshAccessToken(@RequestParam("userName") String userName, @RequestHeader("Authorization") String authorizationHeader) {
        Token token = authService.refresh(userName, authorizationHeader.substring(7));
        return Response.success(token);
    }
}
