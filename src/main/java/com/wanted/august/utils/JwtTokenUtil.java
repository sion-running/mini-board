package com.wanted.august.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public Boolean validate(String token, String userName) {
        String usernameByToken = getUsername(token);
        return usernameByToken.equals(userName) && !isTokenExpired(token);
    }

    public Claims extractAllClaims(String token, String key) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(key))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    public String getUsername(String token) {
        return extractAllClaims(token, secretKey).get("username", String.class);
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token, secretKey).getExpiration();
        return expiration.before(new Date());
    }

    public String generateAccessToken(String username) {
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken() {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs * 7))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }
}
