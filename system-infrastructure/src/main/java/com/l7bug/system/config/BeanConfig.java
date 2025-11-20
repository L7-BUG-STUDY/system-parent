package com.l7bug.system.config;

import com.l7bug.system.domain.role.Role;
import com.l7bug.system.domain.role.RoleGateway;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * BeanConfig
 *
 * @author Administrator
 * @since 2025/11/18 14:56
 */
@Configuration
public class BeanConfig {
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Role role(RoleGateway roleGateway) {
		return new Role(roleGateway);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public User user(UserGateway userGateway) {
		return new User(userGateway);
	}
}
