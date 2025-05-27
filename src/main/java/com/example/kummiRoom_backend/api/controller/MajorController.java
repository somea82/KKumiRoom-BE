package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.dto.responseDto.MajorByAreaResponseDto;
import com.example.kummiRoom_backend.api.dto.responseDto.MajorDetailResponseDto;
import com.example.kummiRoom_backend.api.service.MajorService;
import com.example.kummiRoom_backend.global.apiResult.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/major")
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @GetMapping("/{majorId}")
    public ResponseEntity<?> getCourseDetail(@PathVariable Long majorId) {
        MajorDetailResponseDto detail = majorService.getMajorDetail(majorId);
        return ResponseEntity.ok(new ApiResult(200, "OK", "학과 정보를 성공적으로 불러왔습니다.", detail));
    }

    @GetMapping("/area")
    public ResponseEntity<?> getCoursesByArea(
            @RequestParam(required = false) String majorArea) {
        List<MajorByAreaResponseDto> majorList = majorService.getMajorByArea(majorArea);
        return ResponseEntity.ok(new ApiResult(200, "OK", "계열별 학과 정보를 불러왔습니다.", majorList));
    }
}