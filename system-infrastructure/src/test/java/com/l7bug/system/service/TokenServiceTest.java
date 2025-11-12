package com.l7bug.system.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenServiceTest {
	@Autowired
	private TokenService tokenService;

	@Test
	void tokenValid() {
		boolean flag = tokenService.tokenValid("");
		Assertions.assertFalse(flag);
	}

	@Test
	void expireToken() {
		tokenService.expireToken("");
	}
}
