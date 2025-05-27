package com.example.kummiRoom_backend.api.dto.requestDto;

import com.example.kummiRoom_backend.api.entity.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeTableCreateRequest {
    private String semester;
}