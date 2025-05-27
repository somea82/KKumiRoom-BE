package com.example.kummiRoom_backend.api.dto.requestDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {
	private String authId;
	private String password;
}
