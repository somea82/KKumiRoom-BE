package com.example.kummiRoom_backend.openApi;

import com.example.kummiRoom_backend.api.entity.Course;
import com.example.kummiRoom_backend.api.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
}