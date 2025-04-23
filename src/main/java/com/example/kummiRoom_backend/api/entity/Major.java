package com.example.kummiRoom_backend.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MajorId;

    private String name; // 학과 이름

    private String courseArea; // 계열 예) 사회과학 계열

    private String description;
    private String relatedMajors;
    private String recommendedCourses;
    private String recommendedStudents;
    private String careerPaths;
}
