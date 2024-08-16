package com.wanted.august.service;

import com.wanted.august.model.Comment;
import com.wanted.august.model.entity.CommentEntity;
import com.wanted.august.model.request.CommentCreateRequest;
import com.wanted.august.model.request.CommentUpdateRequest;

public interface CommentService {
    Comment addComment(CommentCreateRequest request, String writerName);
    Comment update(CommentUpdateRequest request, String writerName);

    void delete(Long commentId, String writerName);
}
