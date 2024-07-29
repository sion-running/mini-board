package com.wanted.august.controller;

import com.wanted.august.model.request.UserLoginRequest;
import com.wanted.august.model.response.Response;
import com.wanted.august.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/login")
public class LoginController {
    private final UserService userService;
    @PostMapping
    public Response<String> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request);
        return Response.success(token);
    }
}
