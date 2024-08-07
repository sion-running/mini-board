package com.wanted.august.service;

import com.wanted.august.model.Post;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.model.request.PostUpdateRequest;
import com.wanted.august.model.request.SearchRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Post create(PostCreateRequest request, String userName);

    String update(PostUpdateRequest request, String userName);

    List<Post> searchList(SearchRequest request);
}