package com.l7bug.system.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.github.javafaker.Faker;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.AbstractException;
import com.l7bug.common.page.PageData;
import com.l7bug.common.result.Result;
import com.l7bug.system.context.MdcUserInfoContext;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.domain.user.UserStatus;
import com.l7bug.system.dto.request.LoginRequest;
import com.l7bug.system.dto.request.QueryUserRequest;
import com.l7bug.system.dto.request.UpdateUserRequest;
import com.l7bug.system.dto.response.CurrentUserInfoResponse;
import com.l7bug.system.dto.response.UserInfoResponse;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import com.l7bug.system.mybatis.service.SystemUserService;
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
	@Autowired
	private SystemUserService systemUserService;

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
		Result<CurrentUserInfoResponse> userInfoResponseResult = this.userClient.currentUserInfo();
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
		AbstractException abstractException = Assertions.assertThrows(AbstractException.class, () -> this.userClient.createUser(new UpdateUserRequest(user.getUsername(), user.getNickname(), "123", 1)));
		log.error("e: ", abstractException);
		Assertions.assertEquals(ClientErrorCode.USER_NOT_NULL.getCode(), abstractException.getCode());
		UpdateUserRequest updateUserRequest = new UpdateUserRequest(UUID.randomUUID().toString().replace("-", ""), faker.name().name(), faker.phoneNumber().cellPhone(), 1);
		Result<Void> createUser = this.userClient.createUser(updateUserRequest);
		Assertions.assertTrue(createUser.isSuccess());
		User loginUser = new User(this.userGateway);
		loginUser.setUsername(updateUserRequest.username());
		loginUser.setRawPassword(updateUserRequest.rawPassword());
		String login = loginUser.login();
		Assertions.assertNotNull(login);
		Assertions.assertFalse(login.isBlank());
	}

	@Test
	void updateUserById() {
		Assertions.assertThrows(Exception.class, () -> this.userClient.updateUserById(null, null));
		Assertions.assertThrows(Exception.class, () -> this.userClient.updateUserById(1L, null));
		Assertions.assertThrows(Exception.class, () -> this.userClient.updateUserById(null, new UpdateUserRequest("", "", "", null)));
		AbstractException abstractException = Assertions.assertThrows(AbstractException.class, () -> this.userClient.updateUserById(IdUtil.getSnowflakeNextId(), new UpdateUserRequest("", "", "", null)));
		Assertions.assertEquals(ClientErrorCode.DATA_IS_NULL.getCode(), abstractException.getCode());
		MdcUserInfoContext.putMdcToken(user.login());
		Result<Void> voidResult = this.userClient.updateUserById(user.getId(), new UpdateUserRequest(null, faker.name().name(), faker.phoneNumber().cellPhone(), null));
		Assertions.assertTrue(voidResult.isSuccess());
		SystemUser byId = systemUserService.getById(user.getId());
		Assertions.assertNotNull(byId);
		Assertions.assertEquals(user.getId(), byId.getUpdateBy());
		UpdateUserRequest updateUserRequest = new UpdateUserRequest(UUID.randomUUID().toString(), faker.name().name(), faker.phoneNumber().cellPhone(), UserStatus.ENABLE.ordinal());
		voidResult = this.userClient.updateUserById(user.getId(), updateUserRequest);
		Assertions.assertTrue(voidResult.isSuccess());
		User userById = this.userGateway.getUserById(user.getId());
		Assertions.assertEquals(userById.getUsername(), updateUserRequest.username());
		Assertions.assertEquals(userById.getNickname(), updateUserRequest.nickname());
		Assertions.assertTrue(userById.isEnable());
		updateUserRequest = new UpdateUserRequest(UUID.randomUUID().toString(), faker.name().name(), faker.phoneNumber().cellPhone(), UserStatus.DISABLE.ordinal());
		voidResult = this.userClient.updateUserById(user.getId(), updateUserRequest);
		Assertions.assertTrue(voidResult.isSuccess());
		userById = this.userGateway.getUserById(user.getId());
		Assertions.assertEquals(userById.getUsername(), updateUserRequest.username());
		Assertions.assertEquals(userById.getNickname(), updateUserRequest.nickname());
		Assertions.assertTrue(userById.isDisable());
		System.err.println("测试参数为空的情况");
		updateUserRequest = new UpdateUserRequest(null, null, null, null);
		user = userGateway.getUserById(user.getId());
		this.userClient.updateUserById(user.getId(), updateUserRequest);
		userById = this.userGateway.getUserById(user.getId());
		Assertions.assertEquals(user.getUsername(), userById.getUsername());
		Assertions.assertEquals(user.getNickname(), userById.getNickname());
		Assertions.assertEquals(user.getStatus(), userById.getStatus());
	}

	@Test
	void updateUserByIdPassword() {
		// 测试密码
		user.setRawPassword(faker.phoneNumber().cellPhone());
		user.save();
		User userById = this.userGateway.getUserById(user.getId());
		Assertions.assertEquals(user.getPassword(), userById.getPassword());
		UpdateUserRequest updateUserRequest = new UpdateUserRequest(null, null, faker.phoneNumber().cellPhone(), null);
		this.userClient.updateUserById(user.getId(), updateUserRequest);
		userById = this.userGateway.getUserById(user.getId());
		Assertions.assertNotEquals(user.getPassword(), userById.getPassword());
	}

	@Test
	void pageUser() {
		Result<PageData<UserInfoResponse>> pageDataResult = this.userClient.pageUser(new QueryUserRequest());
		Assertions.assertTrue(pageDataResult.isSuccess());
		Assertions.assertNotNull(pageDataResult.getData());
		Assertions.assertNotNull(pageDataResult.getData().data());
	}

	@Test
	void deleteUserById() {
		user.save();
		this.userClient.deleteUserById(user.getId());
		Assertions.assertThrows(Exception.class, () -> this.userClient.deleteUserById(user.getId()));
	}
}
