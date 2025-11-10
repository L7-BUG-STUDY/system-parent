package com.l7bug.system.client;

import cn.hutool.core.bean.BeanUtil;
import com.l7bug.common.result.Result;
import com.l7bug.system.context.MdcUserInfoContext;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.response.UserInfoResponse;
import com.l7bug.system.security.UserDetailsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@SpringBootTest
class UserClientImplTest {
	@Autowired
	private UserClientImpl userClient;

	@Autowired
	private UserGateway userGateway;
	private User user = new User(userGateway);

	@BeforeEach
	void setUp() {
		user = new User(userGateway);
		user.setUsername(UUID.randomUUID().toString().replace("-", ""));
		user.setRawPassword(UUID.randomUUID().toString().replace("-", ""));
		user.setNickname("测试用户");
		user.setEnable();
		user.save();
		String login = user.login();
		MdcUserInfoContext.putMdcToken(login);
		User user = userGateway.currentUser();
		MdcUserInfoContext.putMdcUserName(user.getUsername());
		MdcUserInfoContext.putMdcNickname(user.getNickname());
		MdcUserInfoContext.putUserId(user.getId().toString());
		UserDetailsImpl userDetails = BeanUtil.copyProperties(user, UserDetailsImpl.class);
		userDetails.setPassword("123456");
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	void login() {
		Result<String> login = userClient.login(new LoginRequest(user.getUsername(), user.getRawPassword()));
		Assertions.assertTrue(login.isSuccess());
	}

	@Test
	void currentUserInfo() {
		String login = user.login();
		MdcUserInfoContext.putMdcToken(login);
		Result<UserInfoResponse> userInfoResponseResult = this.userClient.currentUserInfo();
		Assertions.assertTrue(userInfoResponseResult.isSuccess());
	}

	@Test
	void logout() {
		String login = user.login();
		MdcUserInfoContext.putMdcToken(login);
		this.userClient.logout();
	}

	@Test
	void hasAuthorities() {
		this.userClient.hasAuthorities("READ");
	}
}
