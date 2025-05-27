package com.example.kummiRoom_backend.api.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDto {
	private String currentPassword;
	private String newPassword;
}
