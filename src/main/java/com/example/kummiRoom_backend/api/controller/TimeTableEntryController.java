package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableEntryCreateRequest;
import com.example.kummiRoom_backend.api.dto.responseDto.TimeTableResponseDto;
import com.example.kummiRoom_backend.api.service.TimeTableEntryService;
import com.example.kummiRoom_backend.api.service.TimeTableService;
import com.example.kummiRoom_backend.global.apiResult.ApiResult;
import com.example.kummiRoom_backend.global.auth.AuthService;
import com.example.kummiRoom_backend.global.auth.JwtService;
import com.example.kummiRoom_backend.global.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timeTableEntry")
public class TimeTableEntryController {
    private final TimeTableEntryService timeTableEntryService;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> createTimeTable(HttpServletRequest request, @RequestBody TimeTableEntryCreateRequest dto) {
        String accessToken = authService.getCookieValue(request, "accessToken");
        if(accessToken == null){
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }
        Long userId = jwtService.extractUserId(accessToken);
        timeTableEntryService.createTimeTableEntry(userId,dto);
        return ResponseEntity.ok(new ApiResult(200,"OK","시간표가 정상적으로 갱신 되었습니다."));
    }
}
