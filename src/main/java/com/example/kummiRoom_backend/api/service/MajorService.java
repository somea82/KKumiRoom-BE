package com.example.kummiRoom_backend.api.service;

import com.example.kummiRoom_backend.api.dto.responseDto.MajorByAreaResponseDto;
import com.example.kummiRoom_backend.api.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;

    public List<MajorByAreaResponseDto> getMajorByArea(String majorArea) {

        return majorRepository.findByMajorArea(majorArea).stream()
                .map(MajorByAreaResponseDto::from)
                .collect(Collectors.toList());
    }

//    public CourseDetailDto getCourseDetail(Long courseId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new NotFoundException("해당 학과를 찾을 수 없습니다."));
//        return CourseDetailDto.from(course);
//    }
}
