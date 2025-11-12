package com.l7bug.system.client;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.github.javafaker.Faker;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.AbstractException;
import com.l7bug.common.result.Result;
import com.l7bug.system.context.MdcUserInfoContext;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.dto.request.CreateUserRequest;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.response.UserInfoResponse;
import com.l7bug.system.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;
import java.util.UUID;

@Slf4j
@SpringBootTest
class UserClientImplTest {
	private final Faker faker = new Faker(Locale.CHINA);
	@Autowired
	private UserClientImpl userClient;
	@Autowired
	private UserGateway userGateway;
	private User user;

	@BeforeEach
	void setUp() {
		user = new User(userGateway);
		user.setUsername(UUID.randomUUID().toString().replace("-", ""));
		user.setRawPassword(UUID.randomUUID().toString().replace("-", ""));
		user.setNickname(faker.name().name());
		user.setEnable();
		user.save();
		System.err.println(JSON.toJSONString(user));
		String login = user.login();
		MdcUserInfoContext.putMdcToken(login);
		User user = userGateway.currentUser();
		MdcUserInfoContext.putMdcUserName(user.getUsername());
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
	void createUser() {
		AbstractException abstractException = Assertions.assertThrows(AbstractException.class, () -> this.userClient.createUser(new CreateUserRequest(user.getNickname(), user.getUsername(), "123")));
		log.error("e: ", abstractException);
		Assertions.assertEquals(ClientErrorCode.USER_NOT_NULL.getCode(), abstractException.getCode());
		CreateUserRequest createUserRequest = new CreateUserRequest(faker.name().name(), UUID.randomUUID().toString().replace("-", ""), faker.phoneNumber().cellPhone());
		Result<Void> createUser = this.userClient.createUser(createUserRequest);
		Assertions.assertTrue(createUser.isSuccess());
		User loginUser = new User(this.userGateway);
		loginUser.setUsername(createUserRequest.username());
		loginUser.setRawPassword(createUserRequest.rawPassword());
		String login = loginUser.login();
		Assertions.assertNotNull(login);
		Assertions.assertFalse(login.isBlank());
	}
}
