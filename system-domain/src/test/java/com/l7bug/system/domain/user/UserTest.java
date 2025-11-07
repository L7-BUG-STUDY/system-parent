package com.l7bug.system.domain.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserTest {
	private User user;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setUsername("root");
		user.setPassword("root");
		user.setNickname("root");
		user.setStatus(Status.ENABLE);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void isEnable() {
		boolean enable = user.isEnable();
		Assertions.assertTrue(enable);
	}

	@Test
	void isDisable() {
		boolean disable = user.isDisable();
		Assertions.assertFalse(disable);
	}

	@Test
	void setDisable() {
		user.setDisable();
		Assertions.assertTrue(user.isDisable());
	}

	@Test
	void setEnable() {
		user.setDisable();
		Assertions.assertFalse(user.isEnable());
		user.setEnable();
		Assertions.assertTrue(user.isEnable());
	}


	@Test
	void saveTest() {
		User mockUser = new User();
		mockUser.setId(IdUtil.getSnowflakeNextId());
		mockUser.setUsername("admin");
		UserGateway mock = Mockito.mock(UserGateway.class);
		Mockito.when(mock.getUserByUsername("admin")).thenReturn(mockUser);
		Mockito.when(mock.getUserByUsername("root")).thenReturn(null);
		User admin = BeanUtil.copyProperties(user, User.class);
		user.save(mock);
		admin.setUsername("admin");
		admin.save(mock);
		admin.setId(mockUser.getId());
		admin.save(mock);
	}
}
