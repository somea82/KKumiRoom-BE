package com.example.kummiRoom_backend.api.repository;

import com.example.kummiRoom_backend.api.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    List<Major> findByMajorArea(String majoreArea);
}
