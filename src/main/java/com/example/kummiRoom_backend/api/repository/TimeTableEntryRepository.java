package com.example.kummiRoom_backend.api.repository;

import com.example.kummiRoom_backend.api.entity.TimeTableEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableEntryRepository extends JpaRepository<TimeTableEntry, Long> {

}
