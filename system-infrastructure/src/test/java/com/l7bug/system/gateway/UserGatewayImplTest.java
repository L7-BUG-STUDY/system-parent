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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class UserGatewayImplTest {
	private final Faker faker = new Faker(Locale.CHINA);
	private final Map<String, Map<Object, Object>> redisMap = new ConcurrentHashMap<>();
	@Autowired
	private UserGatewayImpl userGatewayImpl;
	private User user;
	@MockitoSpyBean
	private AuthenticationManager authenticationManager;

	@MockitoSpyBean
	private StringRedisTemplate stringRedisTemplate;

	@BeforeEach
	void setUp() {
		user = new User(userGatewayImpl);
		user.setUsername(faker.phoneNumber().cellPhone());
		user.setRawPassword(faker.internet().password());
		user.setStatus(Status.ENABLE);
		user.setNickname(faker.name().name());
		HashOperations<String, Object, Object> ops = Mockito.mock();
		Mockito.doReturn(ops).when(stringRedisTemplate).opsForHash();
		Mockito.doAnswer(i -> redisMap.containsKey(i.getArgument(0).toString()))
			.when(stringRedisTemplate).hasKey(Mockito.any());
		Mockito.doAnswer(i -> Optional.ofNullable(redisMap.get(i.getArgument(0).toString())).map(item -> item.get(i.getArgument(1))).orElse(null))
			.when(ops).get(Mockito.any(), Mockito.any());

		Mockito.doAnswer(i -> {
				Map<Object, Object> orDefault = redisMap.getOrDefault(i.getArgument(0).toString(), new HashMap<>());
				orDefault.put(i.getArgument(1), i.getArgument(2));
				redisMap.put(i.getArgument(0).toString(), orDefault);
				return null;
			})
			.when(ops)
			.put(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	public void testRedis() {
		String key = UUID.randomUUID().toString();
		redisMap.put(key, new HashMap<>());
		Assertions.assertTrue(stringRedisTemplate.hasKey(key));
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
}
