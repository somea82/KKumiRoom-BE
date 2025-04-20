package com.example.kummiRoom_backend.api.service;

import com.example.kummiRoom_backend.api.dto.responseDto.CourseResponseDto;
import com.example.kummiRoom_backend.api.dto.responseDto.SchoolResponseDto;
import com.example.kummiRoom_backend.api.entity.Course;
import com.example.kummiRoom_backend.api.entity.School;
import com.example.kummiRoom_backend.openApi.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;

    public List<SchoolResponseDto> getAllSchool() {
        List<School> schools = schoolRepository.findAll();

        return schools.stream()
                .map(school -> SchoolResponseDto.builder()
                        .schoolId(school.getSchoolId())
                        .schoolName(school.getSchoolName())
                        .eduId(school.getEduId())
                        .build()
                ).collect(Collectors.toList());
    }
}
