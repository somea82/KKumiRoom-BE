package com.example.kummiRoom_backend.api.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolResponseDto {
    private Long schoolId;
    private String schoolName;
    private String eduId;
}
