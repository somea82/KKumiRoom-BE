package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableCreateRequest;
import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableDeleteRequestDto;
import com.example.kummiRoom_backend.api.dto.responseDto.TimeTableResponseDto;
import com.example.kummiRoom_backend.api.entity.TimeTable;
import com.example.kummiRoom_backend.api.service.TimeTableService;
import com.example.kummiRoom_backend.global.apiResult.ApiResult;
import com.example.kummiRoom_backend.global.auth.AuthService;
import com.example.kummiRoom_backend.global.auth.JwtService;
import com.example.kummiRoom_backend.global.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timeTable")
public class TimeTableController {
    private final TimeTableService timeTableService;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> createTimeTable(@RequestBody TimeTableCreateRequest request) {
        timeTableService.createTimeTable(request);
        return ResponseEntity.ok(new ApiResult(200,"OK","시간표가 정상적으로 갱신 되었습니다."));
    }

    @GetMapping
    public ResponseEntity<?> getTimeTables(HttpServletRequest request) {
        String accessToken = authService.getCookieValue(request, "accessToken");
        if(accessToken == null){
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }
        Long userId = jwtService.extractUserId(accessToken);

        List<TimeTableResponseDto> list = timeTableService.getTimeTablesByUserId(userId);
        return ResponseEntity.ok(new ApiResult(200,"OK","시간표 정보를 정상적으로 불러왔습니다.",list));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteTimeTable(HttpServletRequest request, @RequestBody TimeTableDeleteRequestDto dto) {
        String accessToken = authService.getCookieValue(request, "accessToken");
        if(accessToken == null){
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }
        Long userId = jwtService.extractUserId(accessToken);

        timeTableService.deleteTimeTable(dto,userId);
        return ResponseEntity.ok(new ApiResult(200, "OK", "시간표가 정상적으로 삭제되었습니다."));
    }

}
