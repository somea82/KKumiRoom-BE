package com.example.kummiRoom_backend.api.repository;

import com.example.kummiRoom_backend.api.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
}