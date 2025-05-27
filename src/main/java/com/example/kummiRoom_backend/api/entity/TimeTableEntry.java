package com.example.kummiRoom_backend.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeTableEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeTableEntryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timeTable;

    private Integer period;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
}
