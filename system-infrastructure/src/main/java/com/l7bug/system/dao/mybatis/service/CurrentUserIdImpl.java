package com.l7bug.system.dao.mybatis.service;

import com.l7bug.database.config.DataBaseAutoConfiguration;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * CurrentUserIdImpl
 *
 * @author Administrator
 * @since 2025/11/10 14:41
 */
@AllArgsConstructor
@Component
public class CurrentUserIdImpl implements DataBaseAutoConfiguration.CurrentUserId {
	private final ApplicationContext applicationContext;

	@Override
	public Long getCurrentUserId() {
		return Optional.ofNullable(applicationContext.getBean(UserGateway.class).currentUser()).map(User::getId).orElse(-1L);
	}
}
