package com.l7bug.system.client;

import cn.hutool.core.bean.BeanUtil;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.dto.request.CreateUserRequest;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;

/**
 * UserClientImpl
 *
 * @author Administrator
 * @since 2025/11/10 15:40
 */
@Component
@Validated
@AllArgsConstructor
public class UserClientImpl implements UserClient {
	private final UserGateway userGateway;

	@Override
	public Result<String> login(LoginRequest loginRequest) {
		User user = new User(userGateway);
		user.setUsername(loginRequest.username());
		user.setRawPassword(loginRequest.password());
		String login = user.login();
		return Results.success(login);
	}

	@Override
	public Result<UserInfoResponse> currentUserInfo() {
		User user = userGateway.currentUser();

		return Results.success(new UserInfoResponse(user.getUsername(), user.getNickname(), Collections.singletonList("READ")));
	}

	@Override
	public Result<Void> logout() {
		userGateway.logout();
		return Results.success();
	}

	@Override
	public Result<Void> createUser(CreateUserRequest createUserRequest) {
		User userByUsername = userGateway.getUserByUsername(createUserRequest.username());
		if (userByUsername != null) {
			throw new ClientException(ClientErrorCode.USER_NOT_NULL);
		}
		User user = new User(userGateway);
		BeanUtil.copyProperties(createUserRequest, user);
		user.setRawPassword(createUserRequest.rawPassword());
		user.setEnable();
		user.save();
		return Results.success();
	}
}
