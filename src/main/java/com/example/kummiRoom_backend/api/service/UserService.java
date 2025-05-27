package com.example.kummiRoom_backend.api.service;

import com.example.kummiRoom_backend.api.dto.requestDto.AddMajorRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.ChangePasswordRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.UpdateProfileRequestDto;
import com.example.kummiRoom_backend.api.dto.requestDto.UpdateSchoolInfoRequestDto;
import com.example.kummiRoom_backend.api.dto.responseDto.MajorDto;
import com.example.kummiRoom_backend.api.dto.responseDto.SchoolDto;
import com.example.kummiRoom_backend.api.dto.responseDto.UserProfileResponseDto;
import com.example.kummiRoom_backend.api.entity.Major;
import com.example.kummiRoom_backend.api.entity.School;
import com.example.kummiRoom_backend.api.entity.User;
import com.example.kummiRoom_backend.api.repository.MajorRepository;
import com.example.kummiRoom_backend.api.repository.UserRepository;
import com.example.kummiRoom_backend.global.config.security.CryptoUtil;
import com.example.kummiRoom_backend.global.exception.BadRequestException;
import com.example.kummiRoom_backend.global.exception.NotFoundException;
import com.example.kummiRoom_backend.openApi.SchoolRepository;

import lombok.RequiredArgsConstructor;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final SchoolRepository schoolRepository;

    public UserProfileResponseDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        School school = user.getSchool();
        Major major = user.getInterestMajor();
        MajorDto majorDto = null;
        if (major != null) {
            majorDto = MajorDto.builder()
                    .majorId(major.getMajorId())
                    .majorName(major.getMajorName())
                    .description(major.getDescription())
                    .recommendedCourses(major.getRecommendedCourses())
                    .build();
        }

        return UserProfileResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .address(user.getAddress())
                .imageUrl(user.getImageUrl())
                .interestMajor(majorDto)  // 여기서 안전하게 null 또는 값 세팅
                .grade(user.getGrade())
                .classNum(user.getClassNum())
                .school(SchoolDto.builder()
                        .schoolId(school.getSchoolId())
                        .schoolName(school.getSchoolName())
                        .homepage(school.getHomepage())
                        .build())
                .build();
    }



    public void addMajor(AddMajorRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        Major major = majorRepository.findByMajorName(dto.getMajorName())
                .orElseThrow(() -> new IllegalArgumentException("해당 전공을 찾을 수 없습니다."));

        user.setInterestMajor(major);
        userRepository.save(user);
    }

    public void updateUserProfile(Long userId, UpdateProfileRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setUserName(request.getUserName());
        user.setBirth(request.getBirth());

        if(request.getImageUrl() != null) {
            user.setImageUrl(request.getImageUrl());
        }

        userRepository.save(user);
    }

    public void updateSchoolInfo(Long userId, UpdateSchoolInfoRequestDto request){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        School school = schoolRepository.findBySchoolId(request.getSchoolId());
        if(school == null) {
            throw new NotFoundException("해당 학교를 찾을 수 없습니다.");
        }
        user.setSchool(school);
        user.setGrade(request.getGrade());
        user.setClassNum(request.getClassNum());

        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, @NotNull ChangePasswordRequestDto request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        try {
            String decryptedPassword = CryptoUtil.decrypt(user.getPassword());
            if (!decryptedPassword.equals(request.getCurrentPassword())) {
                throw new BadRequestException("현재 비밀번호가 일치하지 않습니다.");
            }

            String newPassword = request.getNewPassword();
            if (newPassword == null || newPassword.length() < 6 || newPassword.length() > 13) {
                throw new BadRequestException("새 비밀번호는 6자 이상 13자 이하로 입력해주세요.");
            }

            user.setPassword(CryptoUtil.encrypt(newPassword));
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("비밀번호 변경 실패: " + e.getMessage());
        }
    }

}
