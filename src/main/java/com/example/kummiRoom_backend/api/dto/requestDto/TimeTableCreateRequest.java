package com.example.kummiRoom_backend.api.dto.requestDto;

import com.example.kummiRoom_backend.api.entity.DayOfWeek;
import lombok.Data;

@Data
public class TimeTableCreateRequest {
    private Long userId;
    private Long courseId;
    private Integer period;
    private DayOfWeek day;
}