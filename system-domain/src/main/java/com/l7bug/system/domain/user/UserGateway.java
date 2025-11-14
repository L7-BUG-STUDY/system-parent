package com.l7bug.system.domain.user;

import com.l7bug.common.page.PageData;
import com.l7bug.common.page.PageQuery;
import jakarta.validation.constraints.NotNull;

/**
 * UserGateway
 *
 * @author Administrator
 * @since 2025/11/7 10:40
 */
public interface UserGateway {
	boolean save(@NotNull(message = "用户不能为空!") User user);

	User getUserByUsername(String username);

	User getUserById(@NotNull(message = "id不能为空") Long id);

	User currentUser();

	String login(String username, String rawPassword);

	void logout();

	String encode(CharSequence rawPassword);

	boolean matches(CharSequence rawPassword, String encodedPassword);


	PageData<User> page(@NotNull(message = "查询参数不能为空") PageQuery pageQuery);
}
