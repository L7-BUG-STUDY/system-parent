package com.l7bug.system.web;

import com.alibaba.fastjson2.JSONArray;
import com.google.common.io.CharStreams;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.system.client.UserClient;
import com.l7bug.system.config.AppSecurityConfiguration;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.response.CurrentUserInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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

	@DeleteMapping("/auth/logout")
	public Result<Void> logout() {
		return userClient.logout();
	}

	@GetMapping("/user/current-user-info")
	public Result<CurrentUserInfoResponse> currentUserInfo() {
		return userClient.currentUserInfo();
	}

	@GetMapping("/menu-list")
	public Result<JSONArray> menuList() throws IOException {
		try (InputStream inputStream = this.getClass().getResourceAsStream("/menu-list.json")) {
			String read = null;
			if (inputStream != null) {
				read = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			}
			return Results.success(JSONArray.parseArray(read));
		}
	}
}
