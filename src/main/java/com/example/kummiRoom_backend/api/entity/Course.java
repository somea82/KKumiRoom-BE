package com.example.kummiRoom_backend.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    @JsonIgnore
    private School school;

    private String courseName;
    //todo 공통 or 선택과목 수정예정
    private String courseType;
    private String courseArea;
    private String semester;
    private String description;

    private Integer maxStudents;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<TimeTable> timeTables;
}
