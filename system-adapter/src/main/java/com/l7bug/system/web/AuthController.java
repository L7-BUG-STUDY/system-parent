package com.l7bug.system.web;

import com.alibaba.fastjson2.JSONArray;
import com.google.common.io.CharStreams;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.system.config.AppSecurityConfiguration;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.response.CurrentUserInfoResponse;
import com.l7bug.system.dto.response.MenuNodeResponse;
import com.l7bug.system.service.MenuAppService;
import com.l7bug.system.service.UserAppService;
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
	private final UserAppService userAppService;
	private final MenuAppService menuAppService;

	@PostMapping(AppSecurityConfiguration.LOGIN_URL)
	public Result<String> login(@RequestBody LoginRequest loginRequest) {
		return userAppService.login(loginRequest);
	}

	@DeleteMapping("/auth/logout")
	public Result<Void> logout() {
		return userAppService.logout();
	}

	@GetMapping("/user/current-user-info")
	public Result<CurrentUserInfoResponse> currentUserInfo() {
		return userAppService.currentUserInfo();
	}

	@GetMapping("/menu-list")
	public Result<JSONArray> menuList() throws IOException {
		Result<MenuNodeResponse> rootNode = menuAppService.getRootNode();
		try (InputStream inputStream = this.getClass().getResourceAsStream("/menu-list.json")) {
			String read = null;
			if (inputStream != null) {
				read = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			}
			JSONArray data = JSONArray.parseArray(read);
			data.addAll(rootNode.getData().getChildren());
			return Results.success(data);
		}
	}
}
