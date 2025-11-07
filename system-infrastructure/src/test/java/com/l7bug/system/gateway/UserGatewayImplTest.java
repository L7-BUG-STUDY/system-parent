package com.l7bug.system.gateway;

import cn.hutool.core.bean.BeanUtil;
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
		user = new User();
		user.setUsername(faker.phoneNumber().cellPhone());
		user.setPassword(faker.internet().password());
		user.setStatus(Status.ENABLE);
		user.setNickname(faker.name().name());
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void save() {
		// 测试插入
		boolean save = user.save(this.userGatewayImpl);
		Assertions.assertTrue(save);
		Assertions.assertNotNull(user.getId());
		// 测试查询
		User selectUser = this.userGatewayImpl.getUserByUsername(user.getUsername());
		Assertions.assertNotNull(selectUser);
		Assertions.assertEquals(user.getUsername(), selectUser.getUsername());
		Assertions.assertEquals(user.getPassword(), selectUser.getPassword());
		Assertions.assertEquals(user.getNickname(), selectUser.getNickname());
		Assertions.assertEquals(user.getStatus(), selectUser.getStatus());
		// 测试查询空
		User userByUsername = this.userGatewayImpl.getUserByUsername(faker.phoneNumber().cellPhone());
		Assertions.assertNull(userByUsername);
		// 测试禁用
		user.setDisable();
		user.save(this.userGatewayImpl);
		selectUser = this.userGatewayImpl.getUserByUsername(user.getUsername());
		Assertions.assertEquals(Status.DISABLE, selectUser.getStatus());
		// 测试更新失败
		User copyProperties = BeanUtil.copyProperties(user, User.class);
		copyProperties.setId(-111L);
		save = copyProperties.save(this.userGatewayImpl);
		Assertions.assertFalse(save);
	}
}
