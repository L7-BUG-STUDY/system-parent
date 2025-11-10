package com.l7bug.system.web;

import com.l7bug.system.dto.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@Value("${server.port}")
	private String port;

	@Test
	void login() throws Exception {
		ResponseEntity<LoginRequest> root = restTemplate
			.postForEntity("http://localhost:" + port + "/auth/login",
				new LoginRequest("root", "123456"),
				LoginRequest.class);
		System.err.println(root);
	}

	@Test
	void currentUserInfo() {
	}

	@Test
	void hasAuthorities() {
	}

	@Test
	void notLogin() {
	}
}
