package com.example.kummiRoom_backend.global.config.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoConfig {

	@Value("${Crypto.SecretKey}")
	private String secretKey;

	@PostConstruct
	public void init() {
		CryptoUtil.setSecretKey(secretKey);  // static 필드에 값 설정
	}
}
