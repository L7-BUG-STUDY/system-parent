package com.l7bug.system.gateway;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.github.javafaker.Faker;
import com.l7bug.system.domain.user.Status;
import com.l7bug.system.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

@SpringBootTest
class UserGatewayImplTest {
	private final Faker faker = new Faker(Locale.CHINA);
	@Autowired
	private UserGatewayImpl userGatewayImpl;
	private User user;

	@BeforeEach
	void setUp() {
		user = new User(userGatewayImpl);
		user.setUsername(faker.phoneNumber().cellPhone());
		user.setRawPassword(faker.internet().password());
		user.setStatus(Status.ENABLE);
		user.setNickname(faker.name().name());
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void save() {
		// 测试插入
		boolean save = user.save();
		Assertions.assertTrue(save);
		Assertions.assertNotNull(user.getId());
		// 检查加密密码是否生成
		Assertions.assertTrue(StrUtil.isNotBlank(user.getPassword()));
		// 测试查询
		User selectUser = this.userGatewayImpl.getUserByUsername(user.getUsername());
		Assertions.assertNotNull(selectUser);
		Assertions.assertEquals(user.getUsername(), selectUser.getUsername());
		Assertions.assertEquals(user.getNickname(), selectUser.getNickname());
		Assertions.assertEquals(user.getStatus(), selectUser.getStatus());
		Assertions.assertNull(selectUser.getRawPassword());
		// 测试查询空
		User userByUsername = this.userGatewayImpl.getUserByUsername(faker.phoneNumber().cellPhone());
		Assertions.assertNull(userByUsername);
		// 测试禁用
		user.setDisable();
		user.save();
		selectUser = this.userGatewayImpl.getUserByUsername(user.getUsername());
		Assertions.assertEquals(Status.DISABLE, selectUser.getStatus());
		// 测试更新失败
		User copyProperties = BeanUtil.copyProperties(user, User.class);
		copyProperties.setId(-111L);
		save = copyProperties.save();
		Assertions.assertFalse(save);
	}

	@Test
	void login() {
		user.save();
		String login = user.login();
		Assertions.assertTrue(StrUtil.isNotBlank(login));
		Assertions.assertTrue(login.contains(user.getId().toString()));
		user.setRawPassword("12346");
		Exception exception = Assertions.assertThrows(Exception.class, () -> user.login());
		System.err.println(exception);
	}
}
