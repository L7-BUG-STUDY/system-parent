package com.l7bug.system.client;


import com.l7bug.common.page.PageData;
import com.l7bug.common.result.Result;
import com.l7bug.system.dto.request.CreateUserRequest;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.request.QueryUserRequest;
import com.l7bug.system.dto.response.CurrentUserInfoResponse;
import com.l7bug.system.dto.response.UserInfoResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
	Result<CurrentUserInfoResponse> currentUserInfo();

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

	Result<Void> updateUserById(@NotNull(message = "id不能为空") Long id, @NotNull(message = "用户修改对象不能为空") CreateUserRequest updateUserRequest);

	/**
	 * 分页查询用户信息
	 *
	 * @param queryUserRequest 查询对象
	 * @return 分页对象
	 */
	Result<PageData<UserInfoResponse>> pageUser(@NotNull(message = "查询条件不能为空") QueryUserRequest queryUserRequest);
}
