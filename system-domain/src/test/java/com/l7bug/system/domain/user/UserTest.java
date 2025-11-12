package com.l7bug.system.domain.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.AbstractException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserTest {
	private User user;
	private User mockUser;

	@BeforeEach
	void setUp() {
		UserGateway mock = Mockito.mock(UserGateway.class);
		mockUser = new User(mock);
		mockUser.setId(IdUtil.getSnowflakeNextId());
		mockUser.setUsername("admin");
		Mockito.when(mock.getUserByUsername("admin")).thenReturn(mockUser);
		Mockito.when(mock.getUserByUsername("root")).thenReturn(null);
		user = new User(mock);
		user.setUsername("root");
		user.setRawPassword("root");
		user.setNickname("root");
		user.setStatus(Status.ENABLE);
		Mockito.doAnswer(i -> user.getRawPassword())
			.when(mock)
			.encode(Mockito.anyString());
		Mockito.doAnswer(i -> i.getArgument(0).equals(i.getArgument(1)))
			.when(mock)
			.matches(Mockito.anyString(), Mockito.anyString());
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
		User admin = BeanUtil.copyProperties(user, User.class);
		user.save();
		Assertions.assertNotNull(user.getPassword());
		Assertions.assertTrue(StrUtil.isNotBlank(user.getPassword()));
		admin.setUsername("admin");
		AbstractException abstractException = Assertions.assertThrows(AbstractException.class, admin::save);
		Assertions.assertEquals(ClientErrorCode.USER_NOT_NULL.getCode(), abstractException.getCode());
		admin.setId(mockUser.getId());
		admin.save();
	}

	@Test
	void loginTest() {
		user.login();
	}
}
