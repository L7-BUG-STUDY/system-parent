package com.l7bug.system.client;

import com.l7bug.common.result.Result;
import com.l7bug.system.dto.request.CreateUserRequest;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.response.UserInfoResponse;
import jakarta.validation.Valid;

/**
 * UserService
 *
 * @author Administrator
 * @since 2025/11/7 14:25
 */
public interface UserClient {
	/**
	 * 登录
	 *
	 * @param loginRequest 登录请求
	 * @return 返回token字符串
	 */
	Result<String> login(@Valid LoginRequest loginRequest);

	/**
	 * 获取当前用户信息
	 *
	 * @return 用户信息
	 */
	Result<UserInfoResponse> currentUserInfo();

	/**
	 * 退出登录
	 *
	 * @return 操作信息
	 */
	Result<Void> logout();

	/**
	 * 创建用户
	 *
	 * @param createUserRequest 用户
	 * @return 操作信息
	 */
	Result<Void> createUser(@Valid CreateUserRequest createUserRequest);
}
