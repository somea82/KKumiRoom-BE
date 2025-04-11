package com.example.kummiRoom_backend.api.dto.requestDto;

import lombok.*;

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
    private Integer grade;
    private Integer classNumber;
}
