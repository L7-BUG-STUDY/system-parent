package com.l7bug.system.domain.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.AbstractException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class UserTest {
	Lock toBeReturned = new ReentrantLock();
	UserGateway userGateway;
	private User user;
	private User mockUser;

	@BeforeEach
	void setUp() {
		userGateway = Mockito.mock(UserGateway.class);
		mockUser = new User(userGateway);
		mockUser.setId(IdUtil.getSnowflakeNextId());
		mockUser.setUsername("admin");
		Mockito.when(userGateway.getUserByUsername("admin")).thenReturn(mockUser);
		Mockito.when(userGateway.getUserByUsername("root")).thenReturn(null);
		user = new User(userGateway);
		user.setUsername("root");
		user.setRawPassword("root");
		user.setNickname("root");
		user.setStatus(Status.ENABLE);
		Mockito.doAnswer(i -> user.getRawPassword())
			.when(userGateway)
			.encode(Mockito.anyString());
		Mockito.doAnswer(i -> i.getArgument(0).equals(i.getArgument(1)))
			.when(userGateway)
			.matches(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(toBeReturned).when(userGateway).getUserLock(Mockito.anyString());
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
	void lockTest() throws InterruptedException {
		List<Exception> exceptionList = new CopyOnWriteArrayList<>();
		List<User> userList = new CopyOnWriteArrayList<>();
		String username = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 20; i++) {
			User e = new User(userGateway);
			e.setUsername(username);
			if (i == 0) {
				e.setUsername("000");
			}
			e.setRawPassword(UUID.randomUUID().toString().replace("-", ""));
			e.setEnable();
			e.setNickname("测试");
			userList.add(e);
		}
		Mockito.doAnswer(i -> {
			ThreadUtil.sleep(5000);
			if (i.getArgument(0).toString().equals("000")) {
				throw new InterruptedException();
			}
			return null;
		}).when(userGateway).getUserByUsername(username);
		for (User item : userList) {
			item.save();
		}
		ExecutorService executorService = new ThreadPoolExecutor(8, 8, 0, TimeUnit.HOURS, new LinkedBlockingQueue<>());
		for (User item : userList) {
			executorService.execute(() -> {
				System.err.println("sssss:" + item.save());
			});
		}
		executorService.shutdown();
		boolean flag = executorService.awaitTermination(0, TimeUnit.HOURS);
	}
}
