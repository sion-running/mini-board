package com.wanted.august.model;

import com.wanted.august.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostDetail {
    private Long postId;
    private User user;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;

    public PostDetail(Long postId, User user, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount) {
        this.postId = postId;
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
    }
}
