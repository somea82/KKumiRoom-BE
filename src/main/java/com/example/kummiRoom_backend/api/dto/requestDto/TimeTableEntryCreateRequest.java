package com.example.kummiRoom_backend.api.dto.requestDto;

import com.example.kummiRoom_backend.api.entity.DayOfWeek;
import lombok.Data;

@Data
public class TimeTableEntryCreateRequest {
    private Long courseId;
    private Integer period;
    private DayOfWeek day;
}