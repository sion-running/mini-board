package com.wanted.august.controller;

import com.wanted.august.model.User;
import com.wanted.august.model.request.UserJoinRequest;
import com.wanted.august.model.response.Response;
import com.wanted.august.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class JoinController {
    private final UserService userService;

    @PostMapping("/join")
    public Response<User> join(@Valid @RequestBody UserJoinRequest request) {
        User user = userService.join(request);
        return Response.success(user);
    }
}
