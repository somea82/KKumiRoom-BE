package com.example.kummiRoom_backend.api.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDto {
    private Long courseId;
    private String courseName;
    private String courseType;
    private String courseArea;
    private String semester;
    private String description;
    private Integer maxStudents;
    private LocalDateTime createdAt;
}