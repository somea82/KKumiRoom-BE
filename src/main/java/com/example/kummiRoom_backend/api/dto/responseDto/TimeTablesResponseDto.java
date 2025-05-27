package com.example.kummiRoom_backend.api.dto.responseDto;

import com.example.kummiRoom_backend.api.entity.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TimeTablesResponseDto {
    private Long timeTableId;
    private String semester;
}