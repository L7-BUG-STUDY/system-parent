package com.l7bug.system.client;

import com.alibaba.fastjson2.JSON;
import com.github.javafaker.Faker;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.etc.SystemEtc;
import com.l7bug.common.result.Result;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.dto.request.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * TestControllerTest
 *
 * @author Administrator
 * @since 2025/11/12 11:47
 */
@Slf4j
@Import(TestController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestControllerTest {
	private final Faker faker = new Faker(Locale.CHINA);
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;
	private String apiHost;
	private User user;

	@Autowired
	private UserGateway userGateway;

	@BeforeEach
	void setUp() {
		apiHost = "http://localhost:" + port;
		user = new User(userGateway);
		user.setNickname(faker.name().name());
		user.setUsername(UUID.randomUUID().toString().replace("-", ""));
		user.setRawPassword(faker.phoneNumber().cellPhone());
		user.setDisable();
		System.err.println("测试地址:" + apiHost);
		System.err.println("本次测试用到的用户:" + JSON.toJSONString(user));
		MDC.clear();
	}

	@Test
	public void loginError() {
		log.info("开始测试认证失败...");
		ResponseEntity<Result<String>> responseEntity = restTemplate.exchange(
			apiHost + "/auth/login",
			HttpMethod.POST,
			new HttpEntity<>(new LoginRequest(user.getUsername(), user.getRawPassword())),
			new ParameterizedTypeReference<>() {
			}
		);
		Assertions.assertNotNull(responseEntity.getBody());
		log.info("认证失败调用结果:{}", JSON.toJSONString(responseEntity.getBody()));
		Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assertions.assertEquals(ClientErrorCode.LOGIN_ERROR.getCode(), responseEntity.getBody().getCode());
		log.info("开始测试用户被禁用...");
		user.save();
		responseEntity = restTemplate.exchange(
			apiHost + "/auth/login",
			HttpMethod.POST,
			new HttpEntity<>(new LoginRequest(user.getUsername(), user.getRawPassword())),
			new ParameterizedTypeReference<>() {
			}
		);
		Assertions.assertNotNull(responseEntity.getBody());
		log.info("用户被禁用调用结果:{}", JSON.toJSONString(responseEntity.getBody()));
		Assertions.assertEquals(ClientErrorCode.USER_IS_DISABLE.getCode(), responseEntity.getBody().getCode());
		log.info("开始测试未认证调用接口...");
		ResponseEntity<Result<?>> responseVoid = restTemplate.exchange(
			apiHost + "/auth/logout",
			HttpMethod.DELETE,
			new HttpEntity<>(null),
			new ParameterizedTypeReference<>() {
			}
		);
		Assertions.assertNotNull(responseVoid.getBody());
		log.info("未认证调用接口结果:{}", JSON.toJSONString(responseVoid.getBody()));
		Assertions.assertEquals(ClientErrorCode.NOT_AUTHENTICATION.getCode(), responseVoid.getBody().getCode());
	}

	@Test
	public void loginAndLogoutTest() {
		ResponseEntity<Result<String>> responseEntity;
		ResponseEntity<Result<?>> responseVoid;
		log.info("开始测试登录成功");
		user.setEnable();
		user.save();
		responseEntity = restTemplate.exchange(
			apiHost + "/auth/login",
			HttpMethod.POST,
			new HttpEntity<>(new LoginRequest(user.getUsername(), user.getRawPassword())),
			new ParameterizedTypeReference<>() {
			}
		);
		Assertions.assertNotNull(responseEntity.getBody());
		log.info("登录成功调用结果:{}", JSON.toJSONString(responseEntity.getBody()));
		Assertions.assertTrue(responseEntity.getBody().isSuccess());
		String token = responseEntity.getBody().getData();
		log.info("开始测试调用退出登录");
		responseVoid = restTemplate.exchange(
			apiHost + "/auth/logout",
			HttpMethod.DELETE,
			new HttpEntity<>(null, MultiValueMap.fromSingleValue(Map.of(SystemEtc.TOKEN_HEADER, token))),
			new ParameterizedTypeReference<>() {
			}
		);
		log.info("退出登录结果:{}", JSON.toJSONString(responseVoid.getBody()));
		Assertions.assertNotNull(responseVoid.getBody());
		Assertions.assertTrue(responseVoid.getBody().isSuccess());
		log.info("再次使用刚刚的token调用退出接口");
		responseVoid = restTemplate.exchange(
			apiHost + "/auth/logout",
			HttpMethod.DELETE,
			new HttpEntity<>(null, MultiValueMap.fromSingleValue(Map.of(SystemEtc.TOKEN_HEADER, token))),
			new ParameterizedTypeReference<>() {
			}
		);
		log.info("退出结果:{}", JSON.toJSONString(responseVoid.getBody()));
		Assertions.assertNotNull(responseVoid.getBody());
		Assertions.assertTrue(responseVoid.getBody().isFailure());
		Assertions.assertEquals(ClientErrorCode.NOT_AUTHENTICATION.getCode(), responseVoid.getBody().getCode());
	}

	@Test
	public void accessDeniedTest() {
		ResponseEntity<Result<String>> responseEntity;
		ResponseEntity<Result<?>> responseVoid;
		log.info("开始测试未授权");
		user.setEnable();
		user.save();
		responseEntity = restTemplate.exchange(
			apiHost + "/auth/login",
			HttpMethod.POST,
			new HttpEntity<>(new LoginRequest(user.getUsername(), user.getRawPassword())),
			new ParameterizedTypeReference<>() {
			}
		);
		Assertions.assertNotNull(responseEntity.getBody());
		String token = responseEntity.getBody().getData();
		responseVoid = restTemplate.exchange(
			apiHost + "/auth/hasAuthorities/123",
			HttpMethod.GET,
			new HttpEntity<>(null, MultiValueMap.fromSingleValue(Map.of(SystemEtc.TOKEN_HEADER, token))),
			new ParameterizedTypeReference<>() {
			}
		);
		log.info(JSON.toJSONString(responseVoid.getBody()));
		Assertions.assertNotNull(responseVoid.getBody());
		Assertions.assertTrue(responseVoid.getBody().isFailure());
		Assertions.assertEquals(ClientErrorCode.ACCESS_DENIED.getCode(), responseVoid.getBody().getCode());
		responseVoid = restTemplate.exchange(
			apiHost + "/auth/hasAuthorities/READ",
			HttpMethod.GET,
			new HttpEntity<>(null, MultiValueMap.fromSingleValue(Map.of(SystemEtc.TOKEN_HEADER, token))),
			new ParameterizedTypeReference<>() {
			}
		);
		log.info(JSON.toJSONString(responseVoid.getBody()));
		Assertions.assertNotNull(responseVoid.getBody());
		Assertions.assertTrue(responseVoid.getBody().isSuccess());
	}

	@Test
	public void testThrowException() {
		ResponseEntity<Result<String>> responseEntity;
		ResponseEntity<Result<?>> responseVoid;

		user.setEnable();
		user.save();
		responseEntity = restTemplate.exchange(
			apiHost + "/auth/login",
			HttpMethod.POST,
			new HttpEntity<>(new LoginRequest(user.getUsername(), user.getRawPassword())),
			new ParameterizedTypeReference<>() {
			}
		);
		Assertions.assertNotNull(responseEntity.getBody());
		String token = responseEntity.getBody().getData();
		responseVoid = restTemplate.exchange(
			apiHost + "/test/throw/AbstractException",
			HttpMethod.GET,
			new HttpEntity<>(null, MultiValueMap.fromSingleValue(Map.of(SystemEtc.TOKEN_HEADER, token))),
			new ParameterizedTypeReference<>() {
			}
		);
		Assertions.assertNotNull(responseVoid.getBody());
		Assertions.assertTrue(responseVoid.getBody().isFailure());
		responseVoid = restTemplate.exchange(
			apiHost + "/test/throw/AbstractException2",
			HttpMethod.GET,
			new HttpEntity<>(null, MultiValueMap.fromSingleValue(Map.of(SystemEtc.TOKEN_HEADER, token))),
			new ParameterizedTypeReference<>() {
			}
		);
		Assertions.assertNotNull(responseVoid.getBody());
		Assertions.assertTrue(responseVoid.getBody().isFailure());
		responseVoid = restTemplate.exchange(
			apiHost + "/test/throw/Throwable?id=1",
			HttpMethod.GET,
			new HttpEntity<>(null, MultiValueMap.fromSingleValue(Map.of(SystemEtc.TOKEN_HEADER, token, SystemEtc.REQUEST_ID, UUID.randomUUID().toString()))),
			new ParameterizedTypeReference<>() {
			}
		);
		responseVoid = restTemplate.exchange(
			apiHost + "/test/throw/Throwable",
			HttpMethod.GET,
			new HttpEntity<>(null, MultiValueMap.fromSingleValue(Map.of(SystemEtc.TOKEN_HEADER, token))),
			new ParameterizedTypeReference<>() {
			}
		);
		Assertions.assertNotNull(responseVoid.getBody());
		Assertions.assertTrue(responseVoid.getBody().isFailure());
	}
}
