package com.l7bug.system.client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.etc.Headers;
import com.l7bug.common.result.Result;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.dto.request.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TestControllerTest
 *
 * @author Administrator
 * @since 2025/11/12 11:47
 */
@Slf4j
@Import({TestController.class, WorkWxBotApiConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestControllerTest {
	private final Faker faker = new Faker(Locale.CHINA);
	@LocalServerPort
	private int port;
	private String apiHost;
	private User user;
	private RestClient restClient;

	@Autowired
	private WorkWxBotApi workWxBotApi;

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

		this.restClient = RestClient.builder()
			.baseUrl(apiHost)
			.build();
	}

	@Test
	public void loginError() {
		log.info("开始测试认证失败...");
		Result<String> body = restClient.post()
			.uri("/auth/login")
			.contentType(MediaType.APPLICATION_JSON)
			.body(new LoginRequest(user.getUsername(), user.getRawPassword()))
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		Assertions.assertNotNull(body);
		log.info("认证失败调用结果:{}", JSON.toJSONString(body));
		Assertions.assertEquals(ClientErrorCode.LOGIN_ERROR.getCode(), body.getCode());
		log.info("开始测试用户被禁用...");
		user.save();
		body = restClient.post()
			.uri("/auth/login")
			.contentType(MediaType.APPLICATION_JSON)
			.body(new LoginRequest(user.getUsername(), user.getRawPassword()))
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		Assertions.assertNotNull(body);
		log.info("用户被禁用调用结果:{}", JSON.toJSONString(body));
		Assertions.assertEquals(ClientErrorCode.USER_IS_DISABLE.getCode(), body.getCode());
		log.info("开始测试未认证调用接口...");
		Result<?> resultVoid = restClient.delete()
			.uri("/auth/logout")
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		Assertions.assertNotNull(resultVoid);
		log.info("未认证调用接口结果:{}", JSON.toJSONString(resultVoid));
		Assertions.assertEquals(ClientErrorCode.NOT_AUTHENTICATION.getCode(), resultVoid.getCode());
	}

	@Test
	public void loginAndLogoutTest() {
		Result<String> responseEntity;
		Result<?> responseVoid;
		log.info("开始测试登录成功");
		user.setEnable();
		user.save();
		responseEntity = restClient.post()
			.uri("/auth/login")
			.body(new LoginRequest(user.getUsername(), user.getRawPassword()))
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		Assertions.assertNotNull(responseEntity);
		log.info("登录成功调用结果:{}", JSON.toJSONString(responseEntity));
		Assertions.assertTrue(responseEntity.isSuccess());
		String token = responseEntity.getData();
		log.info("开始测试调用退出登录");
		responseVoid = restClient.delete()
			.uri("/auth/logout")
			.header(Headers.TOKEN, token)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		log.info("退出登录结果:{}", JSON.toJSONString(responseVoid));
		Assertions.assertNotNull(responseVoid);
		Assertions.assertTrue(responseVoid.isSuccess());
		log.info("再次使用刚刚的token调用退出接口");
		responseVoid = restClient.delete()
			.uri("/auth/logout")
			.header(Headers.TOKEN, token)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		log.info("退出结果:{}", JSON.toJSONString(responseVoid));
		Assertions.assertNotNull(responseVoid);
		Assertions.assertTrue(responseVoid.isFailure());
		Assertions.assertEquals(ClientErrorCode.NOT_AUTHENTICATION.getCode(), responseVoid.getCode());
	}

	@Test
	public void accessDeniedTest() {
		Result<String> responseEntity;
		Result<?> responseVoid;
		log.info("开始测试未授权");
		user.setEnable();
		user.save();
		responseEntity = restClient.post()
			.uri("/auth/login")
			.body(new LoginRequest(user.getUsername(), user.getRawPassword()))
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		Assertions.assertNotNull(responseEntity);
		String token = responseEntity.getData();
		responseVoid = restClient.get()
			.uri("/auth/hasAuthorities/123")
			.header(Headers.TOKEN, token)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		log.info(JSON.toJSONString(responseVoid));
		Assertions.assertNotNull(responseVoid);
		Assertions.assertTrue(responseVoid.isFailure());
		Assertions.assertEquals(ClientErrorCode.ACCESS_DENIED.getCode(), responseVoid.getCode());
		responseVoid = restClient.get()
			.uri("/auth/hasAuthorities/READ")
			.header(Headers.TOKEN, token)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		log.info(JSON.toJSONString(responseVoid));
		Assertions.assertNotNull(responseVoid);
		Assertions.assertTrue(responseVoid.isSuccess());
	}

	@Test
	public void testThrowException() {
		Result<String> responseEntity;
		Result<?> responseVoid;

		user.setEnable();
		user.save();
		responseEntity = restClient.post()
			.uri("/auth/login")
			.body(new LoginRequest(user.getUsername(), user.getRawPassword()))
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		Assertions.assertNotNull(responseEntity);
		String token = responseEntity.getData();
		responseVoid = restClient.get()
			.uri("/test/throw/AbstractException")
			.header(Headers.TOKEN, token)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		Assertions.assertNotNull(responseVoid);
		Assertions.assertTrue(responseVoid.isFailure());
		responseVoid = restClient.get()
			.uri("/test/throw/AbstractException2")
			.header(Headers.TOKEN, token)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		Assertions.assertNotNull(responseVoid);
		Assertions.assertTrue(responseVoid.isFailure());
		responseVoid = restClient.get()
			.uri("/test/throw/Throwable")
			.header(Headers.TOKEN, token)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});
		Assertions.assertNotNull(responseVoid);
		Assertions.assertTrue(responseVoid.isFailure());
	}

	@Test
	public void ttttttt() {
		try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int i = 0; i < 1; i++) {
				int finalI = i;
				executorService.execute(() -> {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("msgtype", "markdown");
					jsonObject.put("markdown", Map.of("content", "# 标题" + finalI));
					log.info("{}::发送=>{}", finalI, jsonObject);
					var send = workWxBotApi.send(jsonObject, "969b7059-e4f4-4bb0-a0a7-4e5457416359");
					log.info("{}::发送结果:{}", finalI, send);
				});
			}
		}
	}
}
