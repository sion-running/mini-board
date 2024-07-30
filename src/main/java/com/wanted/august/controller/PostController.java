package com.wanted.august.controller;

import com.wanted.august.model.request.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/post")
@RestController
@RequiredArgsConstructor
public class PostController {
    @PostMapping
    public void create(@Valid @RequestBody PostCreateRequest request, Authentication authentication) {
        // 유저 정보 확인

        // 포스트 저장
    }
}
