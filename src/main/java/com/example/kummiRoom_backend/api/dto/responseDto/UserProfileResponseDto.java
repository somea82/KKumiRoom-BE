package com.example.kummiRoom_backend.api.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponseDto {
    private Long userId;
    private String userName;
    private LocalDate birth;
    private String phone;
    private String address;
    private String imageUrl;
    private MajorDto interestMajor;
    private Integer grade;
    private Integer classNum;
    private SchoolDto school;
}
