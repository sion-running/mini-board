package com.wanted.august.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.august.model.Post;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class PostDetailResponse {
    private Long id;
    private UserEntity user;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Long leftDaysForModification;

    public static PostDetailResponse fromEntity(PostEntity entity) {
        return PostDetailResponse.builder()
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
