package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.dto.responseDto.CourseResponseDto;
import com.example.kummiRoom_backend.api.service.CourseService;
import com.example.kummiRoom_backend.global.apiResult.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/openapi/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getCoursesBySchool(@RequestParam("schoolId") Long schoolId) {
        List<CourseResponseDto> response = courseService.getCoursesBySchoolId(schoolId);
        return ResponseEntity.ok(new ApiResult(200,"OK","학교 정보 조회에 성공하였습니다.",response));
    }

    @GetMapping("/{course_id}")
    public ResponseEntity<?> getCourseDetailById(@RequestParam("school_id") Long schoolId,
                                                 @PathVariable("course_id") Long courseId) {
        CourseResponseDto response = courseService.getCourseBySchoolIdAndCourseId(schoolId, courseId);
        return ResponseEntity.ok(new ApiResult(200, "OK", "과목 상세 조회에 성공하였습니다.", response));
    }
}
