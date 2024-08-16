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
public class CommentCreateRequest {
    private Long postId;

    @Size(max = 400)
    private String content;
}
