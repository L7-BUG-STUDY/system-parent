package com.l7bug.system.config;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuGateway;
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
	public User user(UserGateway userGateway) {
		return new User(userGateway);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Menu menu(MenuGateway menuGateway) {
		return new Menu(menuGateway);
	}
}
