package com.wanted.august.model;

import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class Post {
    private Long id;
    private UserEntity user;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static Post fromEntity(PostEntity entity) {
        return Post.builder()
                .id(entity.getId())
                .user(entity.getUser())
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
