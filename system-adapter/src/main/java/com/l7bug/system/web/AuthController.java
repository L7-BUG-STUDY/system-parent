package com.l7bug.system.web;

import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.system.client.UserClient;
import com.l7bug.system.config.AppSecurityConfiguration;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 *
 * @author Administrator
 * @since 2025/11/10 16:46
 */
@AllArgsConstructor
@RestController
public class AuthController {
	private final UserClient userClient;

	@PostMapping(AppSecurityConfiguration.LOGIN_URL)
	public Result<String> login(@RequestBody LoginRequest loginRequest) {
		return userClient.login(loginRequest);
	}

	@GetMapping("/current-user-info")
	public Result<UserInfoResponse> currentUserInfo() {
		return userClient.currentUserInfo();
	}

	@GetMapping("/auth/hasAuthorities/{authorities}")
	public Result<Void> hasAuthorities(@PathVariable("authorities") String authorities) {
		return userClient.hasAuthorities(authorities);
	}

	@GetMapping("/not/login")
	public Result<Void> notLogin() {
		return Results.success();
	}
}
