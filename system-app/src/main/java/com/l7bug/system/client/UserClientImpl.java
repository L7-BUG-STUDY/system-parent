package com.l7bug.system.client;

import cn.hutool.core.bean.BeanUtil;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import com.l7bug.common.page.PageData;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.domain.user.UserStatus;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.request.QueryUserRequest;
import com.l7bug.system.dto.request.UpdateUserRequest;
import com.l7bug.system.dto.response.CurrentUserInfoResponse;
import com.l7bug.system.dto.response.UserInfoResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * UserClientImpl
 *
 * @author Administrator
 * @since 2025/11/10 15:40
 */
@Slf4j
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
	public Result<CurrentUserInfoResponse> currentUserInfo() {
		User user = userGateway.currentUser();

		return Results.success(new CurrentUserInfoResponse(user.getId(), user.getUsername(), user.getNickname(), Collections.singletonList("READ")));
	}

	@Override
	public Result<Void> logout() {
		userGateway.logout();
		return Results.success();
	}

	@Override
	public Result<Void> createUser(@Valid UpdateUserRequest createUserRequest) {
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

	@Override
	public Result<Void> updateUserById(Long id, UpdateUserRequest updateUserRequest) {
		User userById = userGateway.getUserById(id);
		if (userById == null) {
			// 用户瞎请求
			log.warn("[用户非法请求]!!!,数据id:[{}],修改内容:[{}]", id, updateUserRequest);
			throw new ClientException(ClientErrorCode.DATA_IS_NULL);
		}
		BeanUtil.copyProperties(updateUserRequest, userById);
		if (updateUserRequest.status() != null) {
			userById.setStatus(updateUserRequest.status() == 1 ? UserStatus.ENABLE : UserStatus.DISABLE);
		}
		userById.setRawPassword(updateUserRequest.rawPassword());
		userById.save();
		return Results.success();
	}

	@Override
	public Result<PageData<UserInfoResponse>> pageUser(QueryUserRequest queryUserRequest) {
		PageData<User> page = this.userGateway.page(queryUserRequest);
		List<UserInfoResponse> list = page.data().stream().map(item -> {
			UserInfoResponse temp = BeanUtil.copyProperties(item, UserInfoResponse.class);
			temp.setStatus(item.getStatus().ordinal());
			return temp;
		}).toList();
		return Results.success(new PageData<>(page.total(), list));
	}

	@Override
	public Result<Void> deleteUserById(Long id) {
		User userById = userGateway.getUserById(id);
		if (userById == null) {
			// 用户瞎请求
			log.warn("[用户非法请求]!!!,数据id:[{}],修改内容:[删除]", id);
			throw new ClientException(ClientErrorCode.DATA_IS_NULL);
		}
		userById.delete();
		return Results.success();
	}
}
