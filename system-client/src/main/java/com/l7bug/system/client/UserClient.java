package com.l7bug.system.client;

import com.l7bug.common.result.Result;
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
	 * 测试权限
	 *
	 * @return 正常返回表示有用
	 */
	Result<Void> hasAuthorities(String authorities);
}
