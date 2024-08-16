package com.wanted.august.service;

import com.wanted.august.model.entity.PostViewEntity;

public interface PostViewService {
    PostViewEntity addCount(Long postId, String userName);
}
