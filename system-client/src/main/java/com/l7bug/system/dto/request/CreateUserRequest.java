package com.l7bug.system.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * CreateUserRequest
 *
 * @author Administrator
 * @since 2025/11/12 16:52
 */
public record CreateUserRequest(@NotBlank(message = "用户昵称不能为空") String nickname,
								@NotBlank(message = "用户名不能为空") String username,
								@NotBlank(message = "密码不能为空") String rawPassword) {
}
