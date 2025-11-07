package com.l7bug.system.gateway;

import com.l7bug.common.error.ServerErrorCode;
import com.l7bug.common.exception.ServerException;
import com.l7bug.system.convertor.UserConvertor;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import com.l7bug.system.mybatis.service.SystemUserService;
import com.l7bug.system.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

/**
 * UserGatewayImpl
 *
 * @author Administrator
 * @since 2025/11/7 10:56
 */
@Validated
@Component
@AllArgsConstructor
public class UserGatewayImpl implements UserGateway {
	private final SystemUserService systemUserService;
	private final UserConvertor userConvertor;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationContext applicationContext;

	@Override
	public boolean save(User user) {
		SystemUser systemUser = userConvertor.mapDo(user);
		boolean flag = this.systemUserService.saveOrUpdate(systemUser);
		user.setId(systemUser.getId());
		return flag;
	}

	@Override
	public User getUserByUsername(String username) {
		SystemUser systemUser = systemUserService.findByUsername(username);
		if (systemUser == null) {
			return null;
		}
		return userConvertor.mapDomain(systemUser);
	}

	@Override
	public String login(String username, String rawPassword) {
		Authentication authenticate = applicationContext.getBean(AuthenticationManager.class).authenticate(new UsernamePasswordAuthenticationToken(username, rawPassword));
		if (authenticate.getPrincipal() != null && authenticate.getPrincipal() instanceof UserDetailsImpl userDetails) {
			return userDetails.getId() + ":" + UUID.randomUUID();
		}
		throw new ServerException(ServerErrorCode.SERVER_ERROR);
	}

	@Override
	public void logout() {

	}

	@Override
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
