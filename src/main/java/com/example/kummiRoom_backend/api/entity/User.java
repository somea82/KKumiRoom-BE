package com.example.kummiRoom_backend.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    private String authId;
    private String password;
    private String userName;
    private Integer grade;
    private Integer classNum;
    private String address;
    private String phone;
    private LocalDate birth;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<TimeTable> timeTables;
}
