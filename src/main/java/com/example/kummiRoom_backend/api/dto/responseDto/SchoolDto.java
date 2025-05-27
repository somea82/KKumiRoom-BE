package com.example.kummiRoom_backend.api.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolDto {
    private Long schoolId;
    private String schoolName;
    private String homepage;
}
