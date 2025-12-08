package com.l7bug.system.mapstruct;

import com.l7bug.system.domain.user.User;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import jakarta.annotation.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.context.ApplicationContext;

/**
 * UserMapstruct
 *
 * @author Administrator
 * @since 2025/12/5 17:57
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserDoMapstruct {
	@Resource
	private ApplicationContext applicationContext;

	public User createUser() {
		return applicationContext.getBean(User.class);
	}

	public abstract User mapDomain(SystemUser user);

	public abstract SystemUser mapDo(User user);
}
