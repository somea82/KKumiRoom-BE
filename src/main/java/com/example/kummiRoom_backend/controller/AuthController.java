package com.example.kummiRoom_backend.controller;

import com.example.kummiRoom_backend.auth.AuthService;
import com.example.kummiRoom_backend.auth.JwtService;
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

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request, HttpServletResponse response) throws Exception {
        AuthResponseDto authResponse = authService.login(request);

        authService.setRefreshTokenCookie(authResponse.getRefreshToken(), response);
        authService.setAccessTokenCookie(authResponse.getAccessToken(), response);

        return ResponseEntity.ok(new ApiResult(200, "Already Exists", "로그인에 성공했습니다."));
    }
}
