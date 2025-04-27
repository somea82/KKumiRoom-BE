package com.example.kummiRoom_backend.api.dto.requestDto;

import com.example.kummiRoom_backend.api.entity.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableDeleteRequestDto {
    private DayOfWeek day;
    private Integer period;
    private String semester;
}