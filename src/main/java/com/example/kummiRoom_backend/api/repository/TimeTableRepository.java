package com.example.kummiRoom_backend.api.repository;

import com.example.kummiRoom_backend.api.entity.DayOfWeek;
import com.example.kummiRoom_backend.api.entity.TimeTable;
import com.example.kummiRoom_backend.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    List<TimeTable> findByUserUserId(Long userId);
    Optional<TimeTable> findByUserAndDayAndPeriod(User user, DayOfWeek day, Integer period);
}