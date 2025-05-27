package com.example.kummiRoom_backend.api.dto.responseDto;

import com.example.kummiRoom_backend.api.entity.Major;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorDetailResponseDto {
    private Long majorId;
    private String majorName;
    private String description;
    private String relatedMajors;
    private String recommendedCourses;
    private String recommendedStudents;
    private String careerPath;

    public static MajorDetailResponseDto from(Major major) {
        return new MajorDetailResponseDto(
                major.getMajorId(),
                major.getMajorName(),
                major.getDescription(),
                major.getRelatedMajors(),
                major.getRecommendedCourses(),
                major.getRecommendedStudents(),
                major.getCareerPaths()
        );
    }
}
