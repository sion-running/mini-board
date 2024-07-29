package com.wanted.august.controller;

import com.wanted.august.model.request.UserJoinRequest;
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
    public void join(@Valid @RequestBody UserJoinRequest request) {
        userService.join(request);
        log.info("유효성 검증 테스트");
    }
}
