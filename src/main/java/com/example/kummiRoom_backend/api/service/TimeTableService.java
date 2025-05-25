package com.example.kummiRoom_backend.api.service;

import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableCreateRequest;
import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableDeleteRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableEntryCreateRequest;
import com.example.kummiRoom_backend.api.dto.responseDto.TimeTableResponseDto;
import com.example.kummiRoom_backend.api.dto.responseDto.TimeTablesResponseDto;
import com.example.kummiRoom_backend.api.entity.Course;
import com.example.kummiRoom_backend.api.entity.TimeTable;
import com.example.kummiRoom_backend.api.entity.TimeTableEntry;
import com.example.kummiRoom_backend.api.entity.User;
import com.example.kummiRoom_backend.api.repository.CourseRepository;
import com.example.kummiRoom_backend.api.repository.TimeTableEntryRepository;
import com.example.kummiRoom_backend.api.repository.TimeTableRepository;
import com.example.kummiRoom_backend.api.repository.UserRepository;
import com.example.kummiRoom_backend.global.exception.BadRequestException;
import com.example.kummiRoom_backend.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;

    public void createTimeTable(Long userId, TimeTableCreateRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Optional<TimeTable> existing = timeTableRepository.findByUserAndSemester(user, dto.getSemester());

        if (existing.isPresent()) {
            throw new BadRequestException("해당 학기의 시간표가 이미 존재합니다.");
        }

        timeTableRepository.save(TimeTable.builder()
                .user(user)
                .semester(dto.getSemester())
                .build());
    }

    public List<TimeTableResponseDto> getTimeTable(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        String currentSemester = calculateCurrentSemester();

        // 시간표 조회, 없으면 기보 시간표 생성
        TimeTable timeTable = timeTableRepository.findByUserAndSemester(user, currentSemester)
                .orElseGet(() -> {
                    TimeTableCreateRequest request = new TimeTableCreateRequest(currentSemester);
                    createTimeTable(userId, request);
                    return timeTableRepository.findByUserAndSemester(user, currentSemester)
                            .orElseThrow(() -> new BadRequestException("시간표 생성에 실패했습니다."));
                });

        return timeTable.getEntries().stream()
                .map(entry -> TimeTableResponseDto.builder()
                        .courseId(entry.getCourse().getCourseId())
                        .courseName(entry.getCourse().getCourseName())
                        .courseType(entry.getCourse().getCourseType())
                        .period(entry.getPeriod())
                        .day(entry.getDay())
                        .build())
                .collect(Collectors.toList());
    }

    public static String calculateCurrentSemester() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        String semesterPart = (month >= 2 && month <= 7) ? "1" : "2";
        return year + "-" + semesterPart;
    }

//    public void deleteTimeTable(TimeTableDeleteRequestDto dto, Long userId) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
//
//        TimeTable timeTable = timeTableRepository.findByUserAndDayAndPeriod(user, dto.getDay(), dto.getPeriod())
//                .orElseThrow(() -> new NotFoundException("해당하는 시간표를 찾을 수 없습니다."));
//
//        timeTableRepository.delete(timeTable);
//    }
}