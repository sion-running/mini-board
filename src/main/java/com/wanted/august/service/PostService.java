package com.wanted.august.service;

import com.wanted.august.model.Post;
import com.wanted.august.model.request.PostCreateRequest;

public interface PostService {
    Post create(PostCreateRequest request, String userName);
}
