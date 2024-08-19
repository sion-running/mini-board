package com.wanted.august.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    private int pageStart = 0;
    private int pageEnd = 0;
    private int pageSize = 10;
    private String title;
    private String direction = "DESC";
}
