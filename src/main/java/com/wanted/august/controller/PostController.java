package com.wanted.august.controller;

import com.wanted.august.model.Post;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.model.response.Response;
import com.wanted.august.service.PostService;
import com.wanted.august.service.UserService;
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
    private final UserService userService;
    private final PostService postService;

    @PostMapping
    public Response<Post> create(@Valid @RequestBody PostCreateRequest request, Authentication authentication) {
        Post post = postService.create(request, authentication.getName());
        return Response.success(post);
    }
}
