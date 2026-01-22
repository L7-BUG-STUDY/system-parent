package com.l7bug.system.client;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.system.client.impl.UserClientImpl;
import com.l7bug.system.config.AppSecurityConfiguration;
import com.l7bug.system.dto.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
		throw new ClientException(ClientErrorCode.NOT_AUTHENTICATION);
	}

	@GetMapping("/test/throw/AbstractException2")
	public Result<Void> throwAbstractException2() {
		ClientException clientException = new ClientException(ClientErrorCode.NOT_AUTHENTICATION);
		throw new ClientException(ClientErrorCode.NOT_AUTHENTICATION, clientException);
	}

	@GetMapping("/test/throw/Throwable")
	public Result<Void> throwThrowable() throws Throwable {
		throw new Throwable("test");
	}
}
