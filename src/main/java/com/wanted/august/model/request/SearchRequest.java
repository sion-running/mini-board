package com.wanted.august.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class SearchRequest {
    private String keyword;
    private String direction = "DESC";
}
