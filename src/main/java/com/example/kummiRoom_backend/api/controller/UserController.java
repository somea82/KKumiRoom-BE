package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.service.UserService;
import com.example.kummiRoom_backend.global.auth.AuthService;
import com.example.kummiRoom_backend.global.auth.JwtService;
import com.example.kummiRoom_backend.global.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/openapi/users")
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        String accessToken = authService.getCookieValue(request, "accessToken");
        System.out.println("@@@"+accessToken);
        if(accessToken == null){
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }
        Long userId = jwtService.extractUserId(accessToken);

        System.out.println(userId);

        return ResponseEntity.ok(userService.getMyProfile(userId));
    }
}
