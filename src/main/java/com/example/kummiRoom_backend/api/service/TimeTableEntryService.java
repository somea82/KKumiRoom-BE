package com.example.kummiRoom_backend.api.service;

import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableDeleteRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.TimeTableEntryCreateRequest;
import com.example.kummiRoom_backend.api.dto.responseDto.TimeTableResponseDto;
import com.example.kummiRoom_backend.api.entity.Course;
import com.example.kummiRoom_backend.api.entity.TimeTable;
import com.example.kummiRoom_backend.api.entity.TimeTableEntry;
import com.example.kummiRoom_backend.api.entity.User;
import com.example.kummiRoom_backend.api.repository.CourseRepository;
import com.example.kummiRoom_backend.api.repository.TimeTableEntryRepository;
import com.example.kummiRoom_backend.api.repository.TimeTableRepository;
import com.example.kummiRoom_backend.api.repository.UserRepository;
import com.example.kummiRoom_backend.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.kummiRoom_backend.api.service.TimeTableService.calculateCurrentSemester;

@Service
@RequiredArgsConstructor
public class TimeTableEntryService {
    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TimeTableEntryRepository timeTableEntryRepository;


    public void createTimeTableEntry(Long userId, TimeTableEntryCreateRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new NotFoundException("과목을 찾을 수 없습니다."));

        String currentSemester = calculateCurrentSemester();

        TimeTable timeTable = timeTableRepository.findByUserAndSemester(user, currentSemester)
                .orElseGet(() -> {
                    TimeTable newTimeTable = TimeTable.builder()
                            .user(user)
                            .semester(currentSemester)
                            .build();
                    return timeTableRepository.save(newTimeTable);
                });

        //중복 확인
        Optional<TimeTableEntry> existingEntry = timeTable.getEntries().stream()
                .filter(e -> e.getDay().equals(dto.getDay()) && e.getPeriod().equals(dto.getPeriod()))
                .findFirst();

        TimeTableEntry entry;
        // 추가) 이미 존재하면 업데이트
        if (existingEntry.isPresent()) {
            // 과목만 업데이트
            entry = existingEntry.get();
            entry.setCourse(course);
            // 굳이 timetable 저장 필요 X
        } else {
            entry = TimeTableEntry.builder()
                    .timeTable(timeTable)
                    .course(course)
                    .period(dto.getPeriod())
                    .day(dto.getDay())
                    .build();
        }
        timeTableEntryRepository.save(entry);
    }

    public void deleteTimeTableEntry(Long userId, TimeTableDeleteRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        String currentSemester = calculateCurrentSemester();
        TimeTable timeTable = timeTableRepository.findByUserAndSemester(user, currentSemester)
                .orElseThrow(() -> new NotFoundException("해당 학기의 시간표가 존재하지 않습니다."));

        TimeTableEntry entry = timeTableEntryRepository.findByTimeTableAndDayAndPeriod(timeTable, dto.getDay(), dto.getPeriod())
                .orElseThrow(() -> new NotFoundException("해당 시간표 항목이 존재하지 않습니다."));

        timeTableEntryRepository.delete(entry);
    }
}
