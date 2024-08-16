package com.wanted.august.controller;

import com.wanted.august.model.Comment;
import com.wanted.august.model.request.CommentCreateRequest;
import com.wanted.august.model.response.Response;
import com.wanted.august.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public Response<Comment> addComment(@Valid @RequestBody CommentCreateRequest request, Authentication authentication) {
        Comment comment = commentService.addComment(request, authentication.getName());
        return Response.success(comment);
    }
}
