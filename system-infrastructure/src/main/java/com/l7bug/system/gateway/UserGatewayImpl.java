package com.l7bug.system.gateway;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.l7bug.common.error.ServerErrorCode;
import com.l7bug.common.exception.ServerException;
import com.l7bug.common.page.PageData;
import com.l7bug.common.page.PageQuery;
import com.l7bug.system.convertor.UserMapstruct;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import com.l7bug.system.mybatis.service.SystemUserService;
import com.l7bug.system.security.UserDetailsImpl;
import com.l7bug.web.context.MdcUserInfoContext;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
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
	private final UserMapstruct userMapstruct;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationContext applicationContext;
	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean save(User user) {
		SystemUser systemUser = userMapstruct.mapDo(user);
		boolean flag = this.systemUserService.saveOrUpdate(systemUser);
		user.setId(systemUser.getId());
		return flag;
	}

	@Override
	public User getUserByUsername(String username) {
		if (Strings.isNullOrEmpty(username)) {
			return null;
		}
		SystemUser systemUser = systemUserService.findByUsername(username);
		if (systemUser == null) {
			return null;
		}
		return userMapstruct.mapDomain(systemUser);
	}

	@Override
	public User getUserById(Long id) {
		SystemUser data = systemUserService.getById(id);
		return data == null ? null : userMapstruct.mapDomain(data);
	}

	@Override
	public User currentUser() {
		String token = MdcUserInfoContext.getMdcToken();
		if (Strings.isNullOrEmpty(token)) {
			return null;
		}
		var ops = stringRedisTemplate.opsForHash();
		String key = buildRedisKey(token);
		if (!stringRedisTemplate.hasKey(key)) {
			return null;
		}
		User user = new User(this);
		user.setId(Optional.ofNullable(ops.get(key, "id")).map(Object::toString).map(Long::valueOf).orElse(null));
		user.setUsername(Optional.ofNullable(ops.get(key, "username")).map(Object::toString).orElse(""));
		user.setNickname(Optional.ofNullable(ops.get(key, "nickname")).map(Object::toString).orElse(""));
		return user;
	}

	@Override
	public String login(String username, String rawPassword) {
		Authentication authenticate = null;
		authenticate = applicationContext.getBean(AuthenticationManager.class).authenticate(new UsernamePasswordAuthenticationToken(username, rawPassword));
		if (authenticate.getPrincipal() != null && authenticate.getPrincipal() instanceof UserDetailsImpl userDetails) {
			String token = userDetails.getId() + ":" + UUID.randomUUID();
			HashOperations<String, Object, Object> ops = stringRedisTemplate.opsForHash();
			String key = buildRedisKey(token);
			ops.put(key, "id", userDetails.getId().toString());
			ops.put(key, "username", userDetails.getUsername());
			ops.put(key, "nickname", userDetails.getNickname());
			ops.put(key, "authorities", userDetails.getAuthoritiesSet().parallelStream().collect(Collectors.joining(",")));
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
		stringRedisTemplate.delete(buildRedisKey(token));
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	@Override
	public PageData<User> page(PageQuery pageQuery, String username) {
		Page<SystemUser> page = new Page<>();
		page.setCurrent(pageQuery.getCurrent());
		page.setSize(pageQuery.getSize());
		OrderItem orderItem = new OrderItem();
		orderItem.setColumn(pageQuery.getColumn());
		orderItem.setAsc(pageQuery.isAsc());
		page.addOrder(orderItem);
		Page<SystemUser> systemUserPage = this.systemUserService.page(page, Wrappers.lambdaQuery(SystemUser.class).eq(!Strings.isNullOrEmpty(username), SystemUser::getUsername, username));
		List<User> data = systemUserPage.getRecords().stream().map(userMapstruct::mapDomain).toList();
		return new PageData<>(systemUserPage.getTotal(), data);
	}

	@Override
	public boolean deleteById(Long id) {
		return this.systemUserService.removeById(id);
	}

	private String buildRedisKey(String token) {
		return "system:user:token:" + token;
	}
}
