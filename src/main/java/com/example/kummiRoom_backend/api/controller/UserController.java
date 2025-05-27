package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.dto.requestDto.AddMajorRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.ChangePasswordRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.UpdateProfileRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.UpdateSchoolInfoRequestDto;
import com.example.kummiRoom_backend.api.service.UserService;
import com.example.kummiRoom_backend.global.apiResult.ApiResult;
import com.example.kummiRoom_backend.global.auth.AuthService;
import com.example.kummiRoom_backend.global.auth.JwtService;
import com.example.kummiRoom_backend.global.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        String accessToken = authService.getCookieValue(request, "accessToken");
        if(accessToken == null){
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }
        Long userId = jwtService.extractUserId(accessToken);

        return ResponseEntity.ok(new ApiResult(200,"OK","내 정보 불러오기에 성공하였습니다.",userService.getMyProfile(userId)));
    }
    @PostMapping("/me")
    public ResponseEntity<?> updateMyProfile(@RequestBody UpdateProfileRequestDto request, HttpServletRequest httpRequest) {
        String accessToken = authService.getCookieValue(httpRequest, "accessToken");
        if (accessToken == null) {
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }

        Long userId = jwtService.extractUserId(accessToken);
        userService.updateUserProfile(userId, request);

        return ResponseEntity.ok(new ApiResult(200, "OK", "내 정보가 성공적으로 수정되었습니다."));
    }

    @PostMapping("/major")
    public ResponseEntity<?> addMajor(@RequestBody AddMajorRequestDto dto, HttpServletRequest request) {
        String accessToken = authService.getCookieValue(request, "accessToken");
        if(accessToken == null){
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }
        Long userId = jwtService.extractUserId(accessToken);

        userService.addMajor(dto,userId);

        return ResponseEntity.ok(new ApiResult(200,"OK","희망 학과 등록에 성공하였습니다."));
    }

    @PostMapping("/school")
    public ResponseEntity<?> updateSchoolInfo(@RequestBody UpdateSchoolInfoRequestDto request, HttpServletRequest httpRequest) {
        String accessToken = authService.getCookieValue(httpRequest, "accessToken");
        if (accessToken == null) {
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }

        Long userId = jwtService.extractUserId(accessToken);
        userService.updateSchoolInfo(userId, request);

        return ResponseEntity.ok(new ApiResult(200, "OK", "학교 정보가 성공적으로 수정되었습니다."));
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto request, HttpServletRequest httpRequest) {
        String accessToken = authService.getCookieValue(httpRequest, "accessToken");
        if (accessToken == null) {
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }

        Long userId = jwtService.extractUserId(accessToken);
        userService.changePassword(userId, request);

        return ResponseEntity.ok(new ApiResult(200, "OK", "비밀번호가 성공적으로 변경되었습니다."));
    }

}
