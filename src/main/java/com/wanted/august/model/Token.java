package com.wanted.august.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Token {
    private String accessToken;
    private String refreshToken;
}
