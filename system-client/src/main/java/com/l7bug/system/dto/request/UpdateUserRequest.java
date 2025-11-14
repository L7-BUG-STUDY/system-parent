package com.l7bug.system.dto.request;

/**
 * UpdateUserRequest
 *
 * @param username    用户名
 * @param nickname    用户昵称
 * @param rawPassword 用户密码
 * @param status      用户状态，1：启用
 * @author l
 * @since 2025/11/14 20:02
 */
public record UpdateUserRequest(String username, String nickname, String rawPassword, Integer status) {
}
