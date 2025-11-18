package com.l7bug.system.domain.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.AbstractException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

class UserTest {
	UserGateway userGateway;
	private User user;
	private User mockUser;

	@BeforeEach
	void setUp() {
		userGateway = Mockito.mock(UserGateway.class);
		mockUser = new User(userGateway);
		mockUser.setId(System.currentTimeMillis());
		mockUser.setUsername("admin");
		Mockito.when(userGateway.getUserByUsername("admin")).thenReturn(mockUser);
		Mockito.when(userGateway.getUserByUsername("root")).thenReturn(null);
		user = new User(userGateway);
		user.setUsername("root");
		user.setRawPassword("root");
		user.setNickname("root");
		user.setStatus(UserStatus.ENABLE);
		Mockito.doAnswer(i -> i.getArgument(0).toString())
			.when(userGateway)
			.encode(Mockito.anyString());
		Mockito.doAnswer(i -> i.getArgument(0).equals(i.getArgument(1)))
			.when(userGateway)
			.matches(Mockito.anyString(), Mockito.anyString());
		Mockito.doAnswer(i -> mockUser)
			.when(userGateway)
			.getUserById(mockUser.getId());
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

	@Test
	void checkPassword() {
		String password = UUID.randomUUID().toString().replace("-", "");
		mockUser.setRawPassword(password);
		mockUser.save();
		Assertions.assertTrue(mockUser.checkPassword(password));
		Assertions.assertFalse(user.checkPassword(password));
		Assertions.assertFalse(user.checkPassword(""));
	}

	@Test
	void delete() {
		mockUser.delete();
	}
}
