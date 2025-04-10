package com.example.kummiRoom_backend.entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timetable")
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timetableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String semester;

    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL)
    private List<TimeTableEntry> entries = new ArrayList<>();
}
