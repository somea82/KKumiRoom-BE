package com.example.kummiRoom_backend.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.access.token.expiration}")
    private Long accessTokenExpiration;

    @Getter
    @Value("${jwt.refresh.token.expiration}")
    private Long refreshTokenExpiration;

    public String generateAccessToken(String authId, Long userId) {
        String token = buildToken(authId, userId, accessTokenExpiration);
        return token;
    }

    public String generateAccessTestToken(String authId, Long userId) {
        String token = buildToken(authId, userId, accessTokenExpiration);
        return token;
    }

    public String generateRefreshToken(String authId, Long userId) {
        String token = buildToken(authId, userId, refreshTokenExpiration);
        return token;
    }

    public String generateRefreshTestToken(String authId, Long userId) {
        String token = buildToken(authId, userId, refreshTokenExpiration);
        return token;
    }

    private Key getSigningKey() {
        System.out.println(">>> secretKey = " + secret.getBytes()); // 추가
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private String buildToken(String authId, Long userId, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roles", Collections.singletonList("ROLE_USER"));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            // todo 블랙리스트 체크
            //if (redisService.isBlacklisted(token)) {
            //    return false;
            //}

            // JWT 유효성 검증
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""));

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public String extractAuthId(String token) {
        System.out.println(extractAllClaims(token).getSubject());
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace(); // 여기에 실제 에러 로그가 찍힐거야
            throw e;
        }
    }
}
