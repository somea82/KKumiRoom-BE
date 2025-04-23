package com.example.kummiRoom_backend.api.dto.responseDto;

import com.example.kummiRoom_backend.api.entity.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TimeTableResponseDto {
    private Long courseId;
    private String courseName;
    private Integer period;
    private DayOfWeek day;
    private String semester;
}