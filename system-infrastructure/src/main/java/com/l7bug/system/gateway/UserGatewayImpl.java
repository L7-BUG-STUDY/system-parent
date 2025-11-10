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
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
	private final StringRedisTemplate stringRedisTemplate;
	private final RedisTemplate<Object, Object> redisTemplate;

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
	public User currentUser() {
		String token = MDC.get("token");
		if (token == null) {
			return null;
		}
		String key = buildRedisKey(token);
		if (!redisTemplate.hasKey(key)) {
			return null;
		}
		User user = new User(this);
		var ops = redisTemplate.opsForHash();
		user.setId(Optional.ofNullable(ops.get(key, "id")).map(Object::toString).map(Long::valueOf).orElse(-1L));
		user.setUsername(Optional.ofNullable(ops.get(key, "username")).map(Object::toString).orElse(""));
		user.setNickname(Optional.ofNullable(ops.get(key, "nickname")).map(Object::toString).orElse(""));
		return user;
	}

	@Override
	public String login(String username, String rawPassword) {
		Authentication authenticate = applicationContext.getBean(AuthenticationManager.class).authenticate(new UsernamePasswordAuthenticationToken(username, rawPassword));
		if (authenticate.getPrincipal() != null && authenticate.getPrincipal() instanceof UserDetailsImpl userDetails) {
			String token = userDetails.getId() + ":" + UUID.randomUUID();
			HashOperations<String, Object, Object> ops = stringRedisTemplate.opsForHash();
			String key = buildRedisKey(token);
			ops.put(key, "id", userDetails.getId().toString());
			ops.put(key, "username", userDetails.getUsername());
			ops.put(key, "nickname", userDetails.getNickname());
			ops.put(key, "authorities", userDetails.getAuthorities().parallelStream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
			stringRedisTemplate.expire(key, 2, TimeUnit.HOURS);
			return token;
		}
		throw new ServerException(ServerErrorCode.SERVER_ERROR);
	}

	@Override
	public void logout() {
		String token = MDC.get("token");
		if (token == null) {
			return;
		}
		stringRedisTemplate.delete(token);
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	private String buildRedisKey(String token) {
		return "system:user:token:" + token;
	}
}
