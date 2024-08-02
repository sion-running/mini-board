package com.wanted.august.model.request;

import lombok.Data;

@Data
public class SearchRequest {
    private String keyword;
    private String order = "createdAt";
    private String direction = "DESC";
}
