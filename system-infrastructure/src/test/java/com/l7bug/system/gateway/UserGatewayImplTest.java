package com.l7bug.system.gateway;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.github.javafaker.Faker;
import com.l7bug.common.page.PageData;
import com.l7bug.common.page.PageQuery;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserStatus;
import com.l7bug.system.mybatis.mapper.SystemUserMapper;
import com.l7bug.system.mybatis.service.SystemUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Locale;

@SpringBootTest
class UserGatewayImplTest {
	private final Faker faker = new Faker(Locale.CHINA);
	@Autowired
	private UserGatewayImpl userGatewayImpl;
	@Autowired
	private SystemUserService systemUserService;
	private User user;
	@MockitoSpyBean
	private AuthenticationManager authenticationManager;
	@Autowired
	private SystemUserMapper userMapper;

	@BeforeEach
	void setUp() {
		user = new User(userGatewayImpl);
		user.setUsername(faker.phoneNumber().cellPhone());
		user.setRawPassword(faker.internet().password());
		user.setStatus(UserStatus.ENABLE);
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
		Assertions.assertEquals(user.getPassword(), selectUser.getPassword());
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
		Assertions.assertEquals(UserStatus.DISABLE, selectUser.getStatus());
		// 测试更新失败
		User copyProperties = BeanUtil.copyProperties(user, User.class);
		copyProperties.setId(-111L);
		Assertions.assertThrows(Exception.class, copyProperties::save);
	}

	@Test
	void login() {
		Assertions.assertThrows(Exception.class, () -> user.login());
		user.setDisable();
		user.save();
		Assertions.assertThrows(Exception.class, () -> user.login());
		user.setEnable();
		user.save();
		String login = user.login();
		Assertions.assertTrue(StrUtil.isNotBlank(login));
		Assertions.assertTrue(login.contains(user.getId().toString()));
		user.setRawPassword("1234");
		Assertions.assertThrows(Exception.class, () -> user.login());
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.doReturn(authentication).when(authenticationManager).authenticate(Mockito.any());
		Assertions.assertThrows(Exception.class, () -> user.login());
		Mockito.when(authentication.getPrincipal()).thenReturn("");
		Assertions.assertThrows(Exception.class, () -> user.login());
	}

	@Test
	void matchesTest() {
		String encode = this.userGatewayImpl.encode("123456");
		String encode2 = this.userGatewayImpl.encode("123456");
		Assertions.assertNotEquals(encode2, encode);
		Assertions.assertTrue(this.userGatewayImpl.matches("123456", encode2));
	}

	@Test
	void currentUserTest() {
		MDC.clear();
		this.userGatewayImpl.logout();
		user.save();
		String token = user.login();
		System.err.println("登录成功!" + token);
		MDC.put("token", token);
		User currentUser = this.userGatewayImpl.currentUser();
		Assertions.assertNotNull(currentUser);
		System.err.println(currentUser);
		Assertions.assertTrue(token.contains(user.getId().toString()));
		this.userGatewayImpl.logout();
		currentUser = this.userGatewayImpl.currentUser();
		Assertions.assertNull(currentUser);
		MDC.put("token", token + 1);
		Assertions.assertNull(this.userGatewayImpl.currentUser());
		this.userGatewayImpl.logout();
	}

	@Test
	void getUserByUsername() {
		User userByUsername = this.userGatewayImpl.getUserByUsername(null);
		Assertions.assertNull(userByUsername);
		userByUsername = this.userGatewayImpl.getUserByUsername("");
		Assertions.assertNull(userByUsername);
		user.save();
		userByUsername = this.userGatewayImpl.getUserByUsername(user.getUsername());
		Assertions.assertNotNull(userByUsername);
	}

	@Test
	void getUserById() {
		Exception exception = Assertions.assertThrows(Exception.class, () -> this.userGatewayImpl.getUserById(null));
		System.err.println(exception.getMessage());
		user.save();
		User userById = this.userGatewayImpl.getUserById(user.getId());
		Assertions.assertEquals(user.getId(), userById.getId());
		Assertions.assertEquals(user.getUsername(), userById.getUsername());
		Assertions.assertEquals(user.getPassword(), userById.getPassword());
	}

	@Test
	void page() {
		user.save();
		PageQuery pageQuery = new PageQuery();
		pageQuery.setCurrent(0);
		pageQuery.setSize(0);
		pageQuery.setColumn("id");
		pageQuery.setAsc(true);
		PageData<User> page = this.userGatewayImpl.page(pageQuery);
		Assertions.assertEquals(this.systemUserService.count(), page.total());
		Assertions.assertTrue(page.data().isEmpty());
		pageQuery.setCurrent(1);
		pageQuery.setSize(1);
		page = this.userGatewayImpl.page(pageQuery);
		System.err.println(page);
		Assertions.assertEquals(1, page.data().size());
		Assertions.assertEquals("root", page.data().iterator().next().getUsername());
	}

	@Test
	void deleteById() {
		user.save();
		boolean delete = user.delete();
		System.err.println(delete);
		Assertions.assertTrue(delete);
		delete = user.delete();
		System.err.println(delete);
		Assertions.assertFalse(delete);
		User userById = this.userGatewayImpl.getUserById(user.getId());
		Assertions.assertNull(userById);
	}
}
