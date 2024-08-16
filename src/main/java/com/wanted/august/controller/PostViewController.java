package com.wanted.august.controller;

import com.wanted.august.model.response.Response;
import com.wanted.august.service.PostViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/view")
public class PostViewController {
    private final PostViewService postViewService;

    @PostMapping("/{postId}")
    public Response<Void> addViewCount(@PathVariable("postId") Long postId, Authentication authentication) {
        postViewService.addCount(postId, authentication.getName());
        return Response.success();
    }
}
