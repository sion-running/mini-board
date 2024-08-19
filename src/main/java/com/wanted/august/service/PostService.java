package com.wanted.august.service;

import com.wanted.august.model.Post;
import com.wanted.august.model.PostDetail;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.model.request.PostUpdateRequest;
import com.wanted.august.model.request.SearchRequest;
import com.wanted.august.model.response.PostDetailResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    Post create(PostCreateRequest request, String userName);

    PostDetailResponse getPostDetail(Long postId);

    String update(PostUpdateRequest request, String userName);

    Page<PostDetail> search(SearchRequest request);

    void delete(Long postId, Boolean isSoftDelete, String userName);

    PostEntity findByPostIdOrElseThrow(Long postId);
}