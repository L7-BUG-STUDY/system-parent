package com.l7bug.system.domain.user;

import com.google.common.base.Strings;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.AbstractException;
import com.l7bug.common.exception.ClientException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

/**
 * 用户领域对象测试类
 * <p>
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
		assertThat(user.getPassword()).isNotNull();
		assertThat(Strings.isNullOrEmpty(user.getPassword())).isFalse();

		// 尝试使用已存在的用户名保存用户，应该抛出异常
		admin.setUsername("admin");
		AbstractException abstractException = catchThrowableOfType(AbstractException.class, admin::save);
		assertThat(abstractException.getCode()).isEqualTo(ClientErrorCode.USER_NOT_NULL.getCode());

		// 设置用户ID后再次保存，应该成功更新
		admin.setId(mockUser.getId());
		admin.save();
	}

	/**
	 * 测试保存用户功能 - 当用户名不存在时
	 * 验证保存具有唯一用户名的新用户能成功保存
	 */
	@Test
	void saveUserWithUniqueUsername() {
		// 设置mock行为，表示用户名不存在
		Mockito.when(userGateway.getUserByUsername("newuser")).thenReturn(null);
		// 设置保存操作返回true
		Mockito.when(userGateway.save(Mockito.any(User.class))).thenReturn(true);

		// 创建一个新用户
		User newUser = new User(userGateway);
		newUser.setUsername("newuser");
		newUser.setRawPassword("password");
		newUser.setNickname("New User");

		// 保存用户并验证返回值
		boolean result = newUser.save();
		assertThat(result).isTrue();
		// 验证密码已被加密
		assertThat(newUser.getPassword()).isEqualTo("password");
	}

	/**
	 * 测试保存用户功能 - 密码加密处理
	 * 验证当设置了原始密码时，密码会被正确加密
	 */
	@Test
	void saveUserWithRawPasswordEncryption() {
		// 设置mock行为
		Mockito.when(userGateway.getUserByUsername("encryptuser")).thenReturn(null);
		Mockito.when(userGateway.encode("plaintext")).thenReturn("encrypted_password");
		Mockito.when(userGateway.save(Mockito.any(User.class))).thenReturn(true);

		// 创建用户并设置原始密码
		User encryptUser = new User(userGateway);
		encryptUser.setUsername("encryptuser");
		encryptUser.setRawPassword("plaintext");
		encryptUser.setNickname("Encrypt User");

		// 保存用户
		boolean result = encryptUser.save();

		// 验证保存成功
		assertThat(result).isTrue();
		// 验证密码已被加密
		assertThat(encryptUser.getPassword()).isEqualTo("encrypted_password");
		// 验证原始密码仍然是明文
		assertThat(encryptUser.getRawPassword()).isEqualTo("plaintext");
	}

	/**
	 * 测试保存用户功能 - 未设置原始密码
	 * 验证当未设置原始密码时，不会进行密码加密操作
	 */
	@Test
	void saveUserWithoutRawPassword() {
		// 设置mock行为
		Mockito.when(userGateway.getUserByUsername("nopassworduser")).thenReturn(null);
		Mockito.when(userGateway.save(Mockito.any(User.class))).thenReturn(true);

		// 创建用户但不设置原始密码
		User noPasswordUser = new User(userGateway);
		noPasswordUser.setUsername("nopassworduser");
		noPasswordUser.setNickname("No Password User");
		noPasswordUser.setPassword("already_encrypted_password");

		// 保存用户
		boolean result = noPasswordUser.save();

		// 验证保存成功
		assertThat(result).isTrue();
		// 验证密码保持不变
		assertThat(noPasswordUser.getPassword()).isEqualTo("already_encrypted_password");
		// 验证原始密码为空
		assertThat(noPasswordUser.getRawPassword()).isNull();
	}

	/**
	 * 测试保存用户功能 - 用户名校验异常情况
	 * 验证当尝试使用已存在的用户名保存用户时，会抛出正确的异常
	 */
	@Test
	void saveUserWithExistingUsernameThrowsException() {
		// 创建一个已存在的用户
		User existingUser = new User(userGateway);
		existingUser.setId(1L);
		existingUser.setUsername("existinguser");

		// 设置mock行为，表示用户名已存在且ID不同
		Mockito.when(userGateway.getUserByUsername("existinguser")).thenReturn(existingUser);

		// 创建一个新用户但使用相同的用户名
		User newUser = new User(userGateway);
		newUser.setId(2L); // 不同的ID
		newUser.setUsername("existinguser");
		newUser.setRawPassword("password");
		newUser.setNickname("New User");

		// 验证保存操作会抛出异常
		ClientException exception = catchThrowableOfType(ClientException.class, newUser::save);
		assertThat(exception.getMessage()).isEqualTo(ClientErrorCode.USER_NOT_NULL.getMessage());
	}

	/**
	 * 测试保存用户功能 - 更新同一用户
	 * 验证当用户更新自己的信息时（用户名相同但ID相同），不会抛出异常
	 */
	@Test
	void saveSameUserWithSameUsername() {
		// 设置mock行为
		Mockito.when(userGateway.save(Mockito.any(User.class))).thenReturn(true);

		// 创建一个用户
		User sameUser = new User(userGateway);
		sameUser.setId(1L);
		sameUser.setUsername("sameuser");
		sameUser.setRawPassword("password");
		sameUser.setNickname("Same User");

		// 设置mock行为，表示找到了相同用户名和ID的用户
		Mockito.when(userGateway.getUserByUsername("sameuser")).thenReturn(sameUser);

		// 更新用户信息（相同ID）
		sameUser.setNickname("Updated Same User");
		boolean result = sameUser.save();

		// 验证保存成功
		assertThat(result).isTrue();
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
