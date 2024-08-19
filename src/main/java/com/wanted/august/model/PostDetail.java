package com.wanted.august.model;

import com.wanted.august.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostDetail {
//    private PostEntity post;
    private Long postId;
    private String userName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long viewCount;

    public PostDetail(Long postId, String userName, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, long viewCount) {
        this.postId = postId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
    }
}
