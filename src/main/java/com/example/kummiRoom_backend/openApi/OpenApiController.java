package com.example.kummiRoom_backend.openApi;

import com.example.kummiRoom_backend.openApi.dto.requestDto.GetSchoolRequestDto;
import com.example.kummiRoom_backend.openApi.dto.requestDto.NeisTimetableRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/openapi")
public class OpenApiController {
    private final OpenApiService openApiService;

    @Value("https://open.neis.go.kr/hub/hisTimetable")
    private String server1BaseUrl;

    @Value("a3f4664fd08e4aaf8253d94322698bc3")
    private String openApiKey;

    @PostMapping("/load")
    public ResponseEntity<?> getTimetable(@RequestBody NeisTimetableRequestDto request) {
        try {
            return ResponseEntity.ok(openApiService.getTimeTable(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/load-school")
    public ResponseEntity<?> getSchool(@RequestBody GetSchoolRequestDto request) throws Exception {
        try {
            return ResponseEntity.ok(openApiService.getSchoolTable(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}