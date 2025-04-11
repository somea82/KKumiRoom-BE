package com.example.kummiRoom_backend.api.entity;

import jakarta.persistence.*;

@Entity
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
}
