package com.wanted.august.service;

import com.wanted.august.model.Post;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.model.request.PostUpdateRequest;

import java.util.List;

public interface PostService {
    Post create(PostCreateRequest request, String userName);

    void update(PostUpdateRequest request, String userName);

    List<Post> findAllByOrderCreatedAt(String direction);
}
