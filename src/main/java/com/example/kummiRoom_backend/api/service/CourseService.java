package com.example.kummiRoom_backend.api.service;

import com.example.kummiRoom_backend.api.dto.responseDto.CourseResponseDto;
import com.example.kummiRoom_backend.api.entity.Course;
import com.example.kummiRoom_backend.api.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<CourseResponseDto> getCoursesBySchoolId(Long schoolId) {
        List<Course> courses = courseRepository.findAllBySchool_SchoolId(schoolId);

        return courses.stream()
                .map(course -> CourseResponseDto.builder()
                        .courseId(course.getCourseId())
                        .courseName(course.getCourseName())
                        .courseArea(course.getCourseArea())
                        .semester(course.getSemester())
                        .description(course.getDescription())
                        .maxStudents(course.getMaxStudents())
                        .createdAt(course.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());
    }
}