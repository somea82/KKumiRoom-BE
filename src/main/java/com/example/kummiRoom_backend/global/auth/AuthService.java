package com.example.kummiRoom_backend.global.auth;


import com.example.kummiRoom_backend.api.dto.requestDto.AuthRequestDto;
import com.example.kummiRoom_backend.api.dto.responseDto.AuthResponseDto;
import com.example.kummiRoom_backend.api.entity.User;
import com.example.kummiRoom_backend.api.repository.UserRepository;
import com.example.kummiRoom_backend.global.config.security.CryptoUtil;
import com.example.kummiRoom_backend.global.exception.NotFoundException;
import com.example.kummiRoom_backend.global.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    @Getter
    @Value("${jwt.access.token.expiration}")
    private Long accessTokenExpiration;

    @Getter
    @Value("${jwt.refresh.token.expiration}")
    private Long refreshTokenExpiration;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisService redisService;

    public AuthResponseDto login(AuthRequestDto request) throws Exception {
        User user = userRepository.findByAuthId(request.getAuthId())
                .orElseThrow(() -> new NotFoundException("아이디/이메일 또는 비밀번호를 잘못 입력하셨습니다."));

        if (!CryptoUtil.decrypt(user.getPassword()).equals(request.getPassword())) {
            throw new UnauthorizedException("아이디/이메일 또는 비밀번호를 잘못 입력하셨습니다.");
        }

        userRepository.save(user);
        return AuthResponseDto.builder()
                .accessToken(jwtService.generateAccessToken(user.getAuthId(), user.getUserId()))
                .refreshToken(jwtService.generateRefreshToken(user.getAuthId(), user.getUserId()))
                .build();
    }

    public AuthResponseDto generateTokens(String authId, String name, Long userId) {
        String accessToken = jwtService.generateAccessToken(authId, userId);
        String refreshToken = jwtService.generateRefreshToken(authId, userId);

        // Redis에는 Refresh Token만 저장
        redisService.saveRefreshToken(authId, refreshToken, refreshTokenExpiration);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
