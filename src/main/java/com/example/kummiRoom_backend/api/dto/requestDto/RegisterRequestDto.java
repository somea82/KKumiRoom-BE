package com.example.kummiRoom_backend.api.dto.requestDto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    private String authId;
    private String password;
    private String name;
    private Long schoolId;
    private String address;
    private String phone;
    private Integer grade;
    private LocalDate birth;
    private Integer classNumber;
}
