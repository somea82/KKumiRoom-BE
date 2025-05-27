package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.dto.responseDto.CourseResponseDto;
import com.example.kummiRoom_backend.api.dto.responseDto.SchoolResponseDto;
import com.example.kummiRoom_backend.api.service.SchoolService;
import com.example.kummiRoom_backend.global.apiResult.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/openapi/schools")
public class SchoolController {
    private final SchoolService schoolService;


    @GetMapping
    public ResponseEntity<?> getAllSchool() {
        List<SchoolResponseDto> response = schoolService.getAllSchool();
        return ResponseEntity.ok(new ApiResult(200,"OK","학교 정보 조회에 성공하였습니다.",response));
    }
}
