package com.example.kummiRoom_backend.api.dto.responseDto;

import com.example.kummiRoom_backend.api.entity.Major;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorByAreaResponseDto {
    private Long majorId;
    private String Name;


    public static MajorByAreaResponseDto from(Major major) {
        return new MajorByAreaResponseDto(major.getMajorId(),major.getMajorName());
    }
}
