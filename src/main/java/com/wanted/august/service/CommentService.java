package com.wanted.august.service;

import com.wanted.august.model.Comment;
import com.wanted.august.model.entity.CommentEntity;
import com.wanted.august.model.request.CommentCreateRequest;

public interface CommentService {
    Comment addComment(CommentCreateRequest request, String writerName);
}
