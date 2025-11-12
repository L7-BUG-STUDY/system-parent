package com.l7bug.system.domain.user;

import jakarta.validation.constraints.NotNull;

import java.util.concurrent.locks.Lock;

/**
 * UserGateway
 *
 * @author Administrator
 * @since 2025/11/7 10:40
 */
public interface UserGateway {
	boolean save(@NotNull(message = "用户不能为空!") User user);

	User getUserByUsername(String username);

	User currentUser();

	String login(String username, String rawPassword);

	void logout();

	String encode(CharSequence rawPassword);

	boolean matches(CharSequence rawPassword, String encodedPassword);

	Lock getUserLock(String username);
}
