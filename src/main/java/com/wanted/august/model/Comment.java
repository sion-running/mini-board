package com.wanted.august.model;

import com.wanted.august.model.entity.CommentEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Comment {
    private Long id;
    private String userName;
    private Long postId;
    private String content;

    public static Comment fromEntity(CommentEntity entity) {
        return Comment.builder()
                .id(entity.getId())
                .userName(entity.getUserName())
                .content(entity.getContent())
                .build();
    }
}
