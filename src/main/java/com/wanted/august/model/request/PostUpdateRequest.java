package com.wanted.august.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {
    private Long postId;

    @Size(max = 200)
    private String title;
    @Size(max = 1000)
    private String content;
}
