package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.dto.requestDto.AuthRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.RegisterRequestDto;
import com.example.kummiRoom_backend.api.dto.responseDto.AuthResponseDto;
import com.example.kummiRoom_backend.global.apiResult.ApiResult;
import com.example.kummiRoom_backend.global.auth.AuthService;
import com.example.kummiRoom_backend.global.auth.JwtService;
import com.example.kummiRoom_backend.global.auth.RedisService;
import com.example.kummiRoom_backend.global.exception.BadRequestException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final RedisService redisService;

    //사용자 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request, HttpServletResponse response) throws Exception {
        AuthResponseDto authResponse = authService.login(request);

        authService.setRefreshTokenCookie(authResponse.getRefreshToken(), response);
        authService.setAccessTokenCookie(authResponse.getAccessToken(), response);

        return ResponseEntity.ok(new ApiResult(200, "success login", "로그인에 성공했습니다."));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        //accessToken 확인
        String accessToken = authService.getCookieValue(request, "accessToken");

        if(accessToken != null) {
            try{
                String authId = jwtService.extractAuthId(accessToken);
                redisService.deleteRefreshToken(authId); //redis 에서 refresh 토큰 삭제
            }catch(Exception e) {// 토큰 만료거나 파실 실패한 경우 무시(이미 로그아웃 됨)
            }
        }
        // accessToken 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setMaxAge(0);

        // refreshToken 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(0);

        // 쿠키 두 개 모두 응답에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new ApiResult(200, "OK", "로그아웃이 완료되었습니다."));
    }

    //회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResult> register(@RequestBody RegisterRequestDto request) {
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new BadRequestException("올바른 비밀번호를 입력해주세요.");
        }
        authService.register(request);
        return ResponseEntity.ok(new ApiResult(200, "OK", "회원가입이 완료되었습니다."));
    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiResult> refreshToken(HttpServletRequest request,
                                                  HttpServletResponse response) {
        // 쿠키에서 리프레시 토큰을 찾기
        String refreshToken = authService.getCookieValue(request, "refreshToken");
        AuthResponseDto newTokens = authService.refreshToken(refreshToken);

        authService.setRefreshTokenCookie(newTokens.getRefreshToken(), response);
        authService.setAccessTokenCookie(newTokens.getAccessToken(), response);

        return ResponseEntity.ok(new ApiResult(200, "OK", "토큰 갱신 성공"));
    }
}
