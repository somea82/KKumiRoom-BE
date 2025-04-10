package com.example.kummiRoom_backend.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer difficulty;
    private String examStyle;
    private String recommendedTrack;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime createdAt;

    private boolean reported = false;
}