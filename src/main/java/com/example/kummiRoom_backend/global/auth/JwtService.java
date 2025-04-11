package com.example.kummiRoom_backend.global.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Jwts;

@Service
@Slf4j
public class JwtService {

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
}
