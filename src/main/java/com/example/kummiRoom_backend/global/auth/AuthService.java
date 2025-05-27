package com.example.kummiRoom_backend.global.auth;


import com.example.kummiRoom_backend.api.dto.requestDto.AuthRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.RegisterRequestDto;
import com.example.kummiRoom_backend.api.dto.responseDto.AuthResponseDto;
import com.example.kummiRoom_backend.api.entity.User;
import com.example.kummiRoom_backend.api.repository.UserRepository;
import com.example.kummiRoom_backend.global.config.security.CryptoUtil;
import com.example.kummiRoom_backend.global.exception.BadRequestException;
import com.example.kummiRoom_backend.global.exception.ForbiddenException;
import com.example.kummiRoom_backend.global.exception.NotFoundException;
import com.example.kummiRoom_backend.global.exception.UnauthorizedException;
import com.example.kummiRoom_backend.openApi.SchoolRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final SchoolRepository schoolRepository;
    private final JwtService jwtService;
    private final RedisService redisService;

    public AuthResponseDto login(AuthRequestDto request) throws Exception {
        User user = userRepository.findByAuthId(request.getAuthId())
                .orElseThrow(() -> new NotFoundException("아이디/이메일 또는 비밀번호를 잘못 입력하셨습니다."));

        if (!CryptoUtil.decrypt(user.getPassword()).equals(request.getPassword())) {
            throw new UnauthorizedException("아이디/이메일 또는 비밀번호를 잘못 입력하셨습니다.");
        }

        return generateTokens(user.getAuthId(), user.getUserName(), user.getUserId());
    }

    public User register(RegisterRequestDto request) {
        try {
            //todo 비밀번호 rule 설정
            // Validate simple password ( 비밀번호는 6자 이상 12이하 )
            if (request.getPassword() == null || request.getPassword().length() < 6
                    || request.getPassword().length() > 13) {
                throw new BadRequestException("올바른 비밀번호를 입력해주세요.");
            }

//휴대폰 번호 입력
//            String formattedPhone = request.getPhone().replaceAll("-", "");


            Optional<User> existingUser = userRepository.findAll().stream()
                    .filter(user ->
                            user.getAuthId().equals(request.getAuthId())
                    )
                    .findFirst();

            // 기존 사용자가 있는 경우
            if (existingUser.isPresent()) {
                throw new BadRequestException("이미 가입된 사용자 입니다.");
            }
            String formattedPhone = request.getPhone().replaceAll("-", "");

            // 새로운 사용자 생성
            User newUser = User.builder()
                    .authId(request.getAuthId())
                    .userName(request.getName())
                    .school(schoolRepository.findBySchoolId(request.getSchoolId()))
                    .address(request.getAddress())
                    .grade(request.getGrade())
                    .classNum(request.getClassNumber())
                    .phone(formattedPhone)
                    .birth(request.getBirth())
                    .password(CryptoUtil.encrypt(request.getPassword())) // 암호화 후 저장
                    .build();

            return userRepository.save(newUser);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("회원가입 실패: " + e.getMessage());
        }
    }

    public void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60); //1시간 유지

        response.addCookie(refreshTokenCookie);
    }

    public void setAccessTokenCookie(String accessToken, HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60); //1시간 유지

        response.addCookie(accessTokenCookie);
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

    public AuthResponseDto refreshToken(String refreshToken) {
        if (!jwtService.isValidToken(refreshToken)) {
            throw new ForbiddenException("유효하지 않은 리프레시 토큰입니다.");
        }

        String authId = jwtService.extractAuthId(refreshToken);
        Long userId = jwtService.extractUserId(refreshToken);

        // Redis에 저장된 리프레시 토큰과 비교
        String storedRefreshToken = redisService.getRefreshToken(authId);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new UnauthorizedException("토큰이 일치하지 않습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        return generateTokens(authId, user.getUserName(), userId);
    }

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equals(cookieName.trim())) {
                    return cookie.getValue();
                }

            }
        }
        return null;
    }
}
