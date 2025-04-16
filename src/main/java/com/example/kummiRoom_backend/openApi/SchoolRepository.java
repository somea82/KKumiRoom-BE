package com.example.kummiRoom_backend.openApi;

import com.example.kummiRoom_backend.api.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, String> {
}