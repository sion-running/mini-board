package com.wanted.august.service;

import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.Token;
import com.wanted.august.model.entity.RefreshTokenEntity;
import com.wanted.august.repository.RefreshTokenRepository;
import com.wanted.august.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Token refresh(String userName, String refreshToken) {
        // refresh token 검증 후 access token 재발급
        RefreshTokenEntity token = refreshTokenRepository.findByUserName(userName).orElseThrow(() -> new AugustApplicationException(ErrorCode.INVALID_TOKEN));
        token.validateSameToken(refreshToken);
        Boolean isTokenExpired = jwtTokenUtil.isTokenExpired(token.getToken());

        if (isTokenExpired) {
            throw new AugustApplicationException(ErrorCode.INVALID_TOKEN);
        }

        return Token.builder()
                .accessToken(jwtTokenUtil.generateAccessToken(userName))
                .refreshToken(generateRefreshToken(userName))
                .build();
    }

    @Override
    public String generateRefreshToken(String userName) {
        // 새로운 refresh token 생성
        String token = jwtTokenUtil.generateRefreshToken();

        // 기존 토큰이 있을 경우 삭제
        refreshTokenRepository.deleteByUserName(userName);

        // 새로운 토큰 저장
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(token)
                .userName(userName)
                .build();

        refreshTokenRepository.save(refreshToken);

        return token;
    }


}
