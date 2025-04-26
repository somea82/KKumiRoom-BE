package com.example.kummiRoom_backend.api.service;

import com.example.kummiRoom_backend.api.dto.responseDto.SchoolDto;
import com.example.kummiRoom_backend.api.dto.responseDto.UserProfileResponseDto;
import com.example.kummiRoom_backend.api.entity.Major;
import com.example.kummiRoom_backend.api.entity.School;
import com.example.kummiRoom_backend.api.entity.User;
import com.example.kummiRoom_backend.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserProfileResponseDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        School school = user.getSchool();
        Major major = user.getInterestMajor();

        return UserProfileResponseDto.builder()
                .userId(user.getUserId())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .address(user.getAddress())
                .interestMajor(major != null ? major.getMajorName() : null)
                .grade(user.getGrade())
                .classNum(user.getClassNum())
                .school(SchoolDto.builder()
                        .schoolId(school.getSchoolId())
                        .schoolName(school.getSchoolName())
                        .homepage(school.getHomepage())
                        .build())
                .build();
    }
}
