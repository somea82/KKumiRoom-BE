package com.example.kummiRoom_backend.api.entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "school")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;

    private String schoolName;
    private String address;

    private String eduId;      // Field
    private String schoolType; // Field2
    private String homepage;   // Field3

    @OneToMany(mappedBy = "school")
    private List<Course> courses = new ArrayList<>();
}
