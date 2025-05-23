package com.example.kummiRoom_backend.api.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSchoolInfoRequestDto {
	private Long schoolId;
	private Integer grade;
	private Integer classNum;
}
