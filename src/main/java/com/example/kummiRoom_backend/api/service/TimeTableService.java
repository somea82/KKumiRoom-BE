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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public void createTimeTable(Long userId,TimeTableCreateRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new NotFoundException("과목을 찾을 수 없습니다."));

        Optional<TimeTable> existingTimeTable = timeTableRepository.findByUserAndDayAndPeriod(user, dto.getDay(), dto.getPeriod());

        // 추가) 이미 존재하면 업데이트
        if (existingTimeTable.isPresent()) {
            TimeTable timeTable = existingTimeTable.get();
            timeTable.setCourse(course);
            timeTableRepository.save(timeTable);
        } else {
            TimeTable timeTable = TimeTable.builder()
                    .user(user)
                    .course(course)
                    .period(dto.getPeriod())
                    .day(dto.getDay())
                    .build();
            timeTableRepository.save(timeTable);
        }
    }

    public List<TimeTableResponseDto> getTimeTablesByUserId(Long userId) {
        return timeTableRepository.findByUserUserId(userId).stream()
                .map(tt -> TimeTableResponseDto.builder()
                        .courseId(tt.getCourse().getCourseId())
                        .courseName(tt.getCourse().getCourseName())
                        .courseType(tt.getCourse().getCourseType())
                        .period(tt.getPeriod())
                        .day(tt.getDay())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteTimeTable(TimeTableDeleteRequestDto dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        TimeTable timeTable = timeTableRepository.findByUserAndDayAndPeriod(user, dto.getDay(), dto.getPeriod())
                .orElseThrow(() -> new NotFoundException("해당하는 시간표를 찾을 수 없습니다."));

        timeTableRepository.delete(timeTable);
    }
}