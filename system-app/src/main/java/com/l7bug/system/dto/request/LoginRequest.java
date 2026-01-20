package com.l7bug.system.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequest
 *
 * @author Administrator
 * @since 2025/11/7 14:18
 */
public record LoginRequest(@NotBlank(message = "用户名不能为空") String username,
						   @NotBlank(message = "密码不能为空") String password) {

}
