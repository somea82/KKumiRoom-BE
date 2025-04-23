package com.example.kummiRoom_backend.api.dto.requestDto;

import lombok.Data;

@Data
public class TimeTableCreateRequest {

    private Long userId;
    private Long courseId;
    private Integer period;
    private String day;
    private String semester;
}