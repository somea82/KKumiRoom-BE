package com.example.kummiRoom_backend.api.dto.requestDto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileRequestDto {
    private String address;
    private String phone;
    private String userName;
    private LocalDate birth;
}
