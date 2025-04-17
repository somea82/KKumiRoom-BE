package com.example.kummiRoom_backend.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    private String courseName;
    private String courseArea;
    private String semester;
    private String description;

    private Integer maxStudents;

    private LocalDateTime createdAt;
}
