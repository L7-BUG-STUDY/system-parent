package com.l7bug.system.client;

import com.l7bug.common.error.ServerErrorCode;
import com.l7bug.common.exception.AbstractException;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.system.config.AppSecurityConfiguration;
import com.l7bug.system.dto.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serial;

/**
 * TestController
 *
 * @author Administrator
 * @since 2025/11/12 10:00
 */
@TestConfiguration
@RestController
public class TestController {
	@Autowired
	private UserClientImpl userClient;

	@PostMapping(AppSecurityConfiguration.LOGIN_URL)
	public Result<String> login(@RequestBody LoginRequest loginRequest) {
		return userClient.login(loginRequest);
	}

	@DeleteMapping("/auth/logout")
	public Result<Void> logout() {
		return userClient.logout();
	}

	@PreAuthorize("hasAuthority('READ')")
	@GetMapping("/auth/hasAuthorities/READ")
	public Result<Void> hasAuthoritiesRead() {
		return Results.success();
	}

	@PreAuthorize("hasAuthority('123')")
	@GetMapping("/auth/hasAuthorities/123")
	public Result<Void> hasAuthorities() {
		return Results.success();
	}

	@GetMapping("/test/throw/AbstractException")
	public Result<Void> throwAbstractException() {
		throw new AbstractException(ServerErrorCode.SERVER_ERROR) {
			@Serial
			private static final long serialVersionUID = -8117518293821661847L;
		};
	}

	@GetMapping("/test/throw/Throwable")
	public Result<Void> throwThrowable() throws Throwable {
		throw new Throwable("test");
	}
}
