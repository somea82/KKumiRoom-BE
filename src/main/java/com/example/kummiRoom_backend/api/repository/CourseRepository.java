package com.example.kummiRoom_backend.api.repository;

import com.example.kummiRoom_backend.api.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllBySchool_SchoolId(Long schoolId);

    Optional<Course> findBySchool_SchoolIdAndCourseId(Long schoolId, Long courseId);
}
