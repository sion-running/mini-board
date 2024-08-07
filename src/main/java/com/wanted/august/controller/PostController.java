package com.wanted.august.controller;

import com.wanted.august.model.Post;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.model.request.PostUpdateRequest;
import com.wanted.august.model.request.SearchRequest;
import com.wanted.august.model.response.Response;
import com.wanted.august.service.PostService;
import com.wanted.august.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PutMapping
    public Response<String> update(@Valid @RequestBody PostUpdateRequest request, Authentication authentication) {
        String message = postService.update(request, authentication.getName());
        return Response.success(message);
    }

//    @GetMapping
//    public Response<List<Post>> getListByCreatedAt(@RequestParam(value = "order", defaultValue = "ASC") String order) {
//        List<Post> list = postService.findAllByOrderCreatedAt(order);
//        return Response.success(list);
//    }

    @GetMapping
    public Response<List<Post>> list(@RequestBody SearchRequest request) {
        List<Post> postList = postService.searchList(request);
        return Response.success(postList);
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable("postId") Long postId,
                                 @RequestParam(value = "isSoftDelete", defaultValue = "true") Boolean isSoftDelete,
                                 Authentication authentication) {
        postService.delete(postId, isSoftDelete, authentication.getName());
        return Response.success();
    }
}
