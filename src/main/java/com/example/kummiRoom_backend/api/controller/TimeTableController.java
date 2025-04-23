package com.example.kummiRoom_backend.api.controller;

import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableCreateRequest;
import com.example.kummiRoom_backend.api.entity.TimeTable;
import com.example.kummiRoom_backend.api.service.TimeTableService;
import com.example.kummiRoom_backend.global.apiResult.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/openapi/timeTable")
public class TimeTableController {
    private final TimeTableService timeTableService;

    @PostMapping
    public ResponseEntity<?> createTimeTable(@RequestBody TimeTableCreateRequest request) {
        timeTableService.createTimeTable(request);
        return ResponseEntity.ok(new ApiResult(200,"OK","시간표가 정상적으로 갱신 되었습니다."));
    }

}
