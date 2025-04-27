package com.example.kummiRoom_backend.api.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorDto {
    private Long majorId;
    private String majorName;
    private String description;
    private String recommendedCourses;

}
