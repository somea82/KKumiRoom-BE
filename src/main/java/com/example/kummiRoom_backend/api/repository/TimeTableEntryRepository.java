package com.example.kummiRoom_backend.api.repository;

import com.example.kummiRoom_backend.api.entity.DayOfWeek;
import com.example.kummiRoom_backend.api.entity.TimeTable;
import com.example.kummiRoom_backend.api.entity.TimeTableEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeTableEntryRepository extends JpaRepository<TimeTableEntry, Long> {
    Optional<TimeTableEntry> findByTimeTableAndDayAndPeriod(TimeTable timeTable, DayOfWeek day, Integer period);
}
