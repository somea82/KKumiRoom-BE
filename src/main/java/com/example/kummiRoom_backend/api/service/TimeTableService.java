package com.example.kummiRoom_backend.api.service;

import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableCreateRequest;
import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableDeleteRequestDto;
import com.example.kummiRoom_backend.api.dto.responseDto.TimeTableResponseDto;
import com.example.kummiRoom_backend.api.entity.Course;
import com.example.kummiRoom_backend.api.entity.TimeTable;
import com.example.kummiRoom_backend.api.entity.User;
import com.example.kummiRoom_backend.api.repository.CourseRepository;
import com.example.kummiRoom_backend.api.repository.TimeTableRepository;
import com.example.kummiRoom_backend.api.repository.UserRepository;
import com.example.kummiRoom_backend.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public void createTimeTable(TimeTableCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new NotFoundException("과목을 찾을 수 없습니다."));

        TimeTable timeTable = TimeTable.builder()
                .user(user)
                .course(course)
                .period(request.getPeriod())
                .day(request.getDay())
                .semester(course.getSemester())
                .build();

        timeTableRepository.save(timeTable);
    }

    public List<TimeTableResponseDto> getTimeTablesByUserId(Long userId) {
        return timeTableRepository.findByUserUserId(userId).stream()
                .map(tt -> TimeTableResponseDto.builder()
                        .courseId(tt.getCourse().getCourseId())
                        .courseName(tt.getCourse().getCourseName())
                        .courseType(tt.getCourse().getCourseType())
                        .period(tt.getPeriod())
                        .semester(tt.getSemester())
                        .day(tt.getDay())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteTimeTable(TimeTableDeleteRequestDto dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // userId + 요일 + 교시로 TimeTable 조회
        TimeTable timeTable = timeTableRepository.findByUserAndDayAndPeriod(user, dto.getDay(), dto.getPeriod())
                .orElseThrow(() -> new NotFoundException("해당하는 시간표를 찾을 수 없습니다."));

        timeTableRepository.delete(timeTable);
    }
}