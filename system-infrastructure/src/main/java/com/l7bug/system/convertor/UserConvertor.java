package com.l7bug.system.convertor;

import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserStatus;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * UserConvertor
 *
 * @author Administrator
 * @since 2025/11/7 12:14
 */
@AllArgsConstructor
@Component
@Deprecated
public class UserConvertor {
	private final ApplicationContext applicationContext;

	public User mapDomain(SystemUser systemUser) {
		User user = applicationContext.getBean(User.class);
		BeanUtils.copyProperties(systemUser, user);
		user.setStatus(systemUser.getStatus() == 1 ? UserStatus.ENABLE : UserStatus.DISABLE);
		return user;
	}

	public SystemUser mapDo(User user) {
		if (user == null) {
			return null;
		}
		SystemUser systemUser = new SystemUser();
		BeanUtils.copyProperties(user, systemUser);
		systemUser.setStatus(Optional.of(user).map(User::getStatus).map(UserStatus::ordinal).orElse(null));
		return systemUser;
	}
}
