package com.example.kummiRoom_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "timetable_entry")
public class TimeTableEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private Integer period;
    private String day;
}
