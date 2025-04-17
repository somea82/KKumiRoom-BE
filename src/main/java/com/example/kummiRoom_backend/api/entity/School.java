package com.example.kummiRoom_backend.api.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "school")
@NoArgsConstructor
@AllArgsConstructor
public class School {

    @Id
    private Long schoolId;

    private String schoolName;
    private String address;

    private String eduId;
    private String schoolType;
    private String homepage;


    @OneToMany(mappedBy = "school")
    private List<Course> courses = new ArrayList<>();
}
