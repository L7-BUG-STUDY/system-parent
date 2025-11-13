package com.l7bug.system.convertor;

import cn.hutool.core.bean.BeanUtil;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.domain.user.UserStatus;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * UserConvertor
 *
 * @author Administrator
 * @since 2025/11/7 12:14
 */
@AllArgsConstructor
@Component
public class UserConvertor {
	private final ApplicationContext applicationContext;

	public User mapDomain(SystemUser systemUser) {
		User user = new User(applicationContext.getBean(UserGateway.class));
		BeanUtil.copyProperties(systemUser, user);
		user.setStatus(systemUser.getStatus() == 1 ? UserStatus.ENABLE : UserStatus.DISABLE);
		return user;
	}

	public SystemUser mapDo(User user) {
		SystemUser systemUser = BeanUtil.copyProperties(user, SystemUser.class);
		systemUser.setStatus(user.getStatus().ordinal());
		return systemUser;
	}
}
