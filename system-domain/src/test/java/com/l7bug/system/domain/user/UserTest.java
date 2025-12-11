package com.l7bug.system.domain.user;

import com.google.common.base.Strings;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.AbstractException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

/**
 * 用户领域对象测试类
 * 
 * 包含对User领域对象各种操作的测试，如状态设置、保存、登录、密码校验等操作
 */
class UserTest {
	UserGateway userGateway;
	private User user;
	private User mockUser;

	/**
	 * 在每个测试方法执行前进行初始化工作
	 * 创建mock的UserGateway对象以及测试用的User对象
	 */
	@BeforeEach
	void setUp() {
		// 创建mock的用户网关对象
		userGateway = Mockito.mock(UserGateway.class);
		
		// 创建模拟的用户对象
		mockUser = new User(userGateway);
		mockUser.setId(System.currentTimeMillis());
		mockUser.setUsername("admin");
		
		// 配置mock对象的预期行为
		Mockito.when(userGateway.getUserByUsername("admin")).thenReturn(mockUser);
		Mockito.when(userGateway.getUserByUsername("root")).thenReturn(null);
		
		// 创建测试用的用户对象
		user = new User(userGateway);
		user.setUsername("root");
		user.setRawPassword("root");
		user.setNickname("root");
		user.setStatus(UserStatus.ENABLE);
		
		// 配置密码编码和匹配的mock行为
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

	/**
	 * 在每个测试方法执行后进行清理工作
	 */
	@AfterEach
	void tearDown() {
		// 可以在这里添加清理资源的代码
	}

	/**
	 * 测试用户启用状态判断功能
	 * 验证新创建的启用状态用户能正确返回true
	 */
	@Test
	void isEnable() {
		// 调用isEnable方法检查用户是否处于启用状态
		boolean enable = user.isEnable();
		// 断言用户处于启用状态
		Assertions.assertTrue(enable);
	}

	/**
	 * 测试用户禁用状态判断功能
	 * 验证新创建的启用状态用户能正确返回false（未被禁用）
	 */
	@Test
	void isDisable() {
		// 调用isDisable方法检查用户是否处于禁用状态
		boolean disable = user.isDisable();
		// 断言用户不处于禁用状态
		Assertions.assertFalse(disable);
	}

	/**
	 * 测试设置用户为禁用状态功能
	 * 验证调用setDisable方法后用户状态变为禁用
	 */
	@Test
	void setDisable() {
		// 调用setDisable方法将用户设置为禁用状态
		user.setDisable();
		// 断言用户确实处于禁用状态
		Assertions.assertTrue(user.isDisable());
	}

	/**
	 * 测试设置用户为启用状态功能
	 * 验证调用setEnable方法后用户状态变为启用
	 */
	@Test
	void setEnable() {
		// 先将用户设置为禁用状态
		user.setDisable();
		// 断言用户确实处于禁用状态
		Assertions.assertFalse(user.isEnable());
		// 再将用户设置为启用状态
		user.setEnable();
		// 断言用户确实处于启用状态
		Assertions.assertTrue(user.isEnable());
	}


	/**
	 * 测试保存用户功能
	 * 验证用户保存时的行为：
	 * 1. 正常保存新用户
	 * 2. 保存已存在的用户名应抛出异常
	 * 3. 更新已有用户信息
	 */
	@Test
	void saveTest() {
		// 创建一个管理员用户用于测试
		User admin = new User(userGateway);
		admin.setUsername("root");
		admin.setRawPassword("root");
		admin.setNickname("root");
		admin.setStatus(UserStatus.ENABLE);
		
		// 保存用户
		user.save();
		// 断言用户密码不为空
		Assertions.assertNotNull(user.getPassword());
		Assertions.assertFalse(Strings.isNullOrEmpty(user.getPassword()));
		
		// 尝试使用已存在的用户名保存用户，应该抛出异常
		admin.setUsername("admin");
		AbstractException abstractException = Assertions.assertThrows(AbstractException.class, admin::save);
		Assertions.assertEquals(ClientErrorCode.USER_NOT_NULL.getCode(), abstractException.getCode());
		
		// 设置用户ID后再次保存，应该成功更新
		admin.setId(mockUser.getId());
		admin.save();
	}

	/**
	 * 测试用户登录功能
	 * 验证用户登录流程能正常执行
	 */
	@Test
	void loginTest() {
		// 调用login方法执行登录流程
		user.login();
	}

	/**
	 * 测试密码校验功能
	 * 验证用户密码校验的准确性：
	 * 1. 正确密码应返回true
	 * 2. 错误密码应返回false
	 * 3. 空密码应返回false
	 */
	@Test
	void checkPassword() {
		// 生成随机密码用于测试
		String password = UUID.randomUUID().toString().replace("-", "");
		// 设置模拟用户的密码
		mockUser.setRawPassword(password);
		// 保存用户（会进行密码编码）
		mockUser.save();
		// 验证正确密码能通过校验
		Assertions.assertTrue(mockUser.checkPassword(password));
		// 验证错误密码不能通过校验
		Assertions.assertFalse(user.checkPassword(password));
		// 验证空密码不能通过校验
		Assertions.assertFalse(user.checkPassword(""));
	}

	/**
	 * 测试删除用户功能
	 * 验证用户删除功能能正常执行
	 */
	@Test
	void delete() {
		// 调用delete方法删除模拟用户
		mockUser.delete();
	}
}