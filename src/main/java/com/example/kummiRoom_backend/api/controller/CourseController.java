package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.dto.responseDto.CourseResponseDto;
import com.example.kummiRoom_backend.api.entity.User;
import com.example.kummiRoom_backend.api.repository.UserRepository;
import com.example.kummiRoom_backend.api.service.CourseService;
import com.example.kummiRoom_backend.global.apiResult.ApiResult;
import com.example.kummiRoom_backend.global.auth.AuthService;
import com.example.kummiRoom_backend.global.auth.JwtService;
import com.example.kummiRoom_backend.global.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getCoursesBySchool(HttpServletRequest request) {
        String accessToken = authService.getCookieValue(request, "accessToken");
        if(accessToken == null){
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }
        Long userId = jwtService.extractUserId(accessToken);

        Optional<User> user = userRepository.findById(userId);

        List<CourseResponseDto> response = courseService.getCoursesBySchoolId(user.get().getSchool().getSchoolId());
        return ResponseEntity.ok(new ApiResult(200,"OK","학교수업 정보 조회에 성공하였습니다.",response));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourseDetailById(HttpServletRequest request, @PathVariable("courseId") Long courseId) {
        String accessToken = authService.getCookieValue(request, "accessToken");
        if(accessToken == null){
            throw new UnauthorizedException("액세스 토큰이 없습니다");
        }
        Long userId = jwtService.extractUserId(accessToken);

        Optional<User> user = userRepository.findById(userId);

        CourseResponseDto response = courseService.getCourseBySchoolIdAndCourseId(user.get().getSchool().getSchoolId(), courseId);
        return ResponseEntity.ok(new ApiResult(200, "OK", "과목 상세 조회에 성공하였습니다.", response));
    }
}
