package com.l7bug.system.client;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * WorkWxBotApiConfig
 *
 * @author Administrator
 * @since 2026/1/19 13:53
 */
@TestConfiguration
public class WorkWxBotApiConfig {

	@Bean
	public WorkWxBotApi externalApiClient() {
		// 1. 创建底层 RestClient
		RestClient restClient = RestClient.builder()
			.baseUrl("https://qyapi.weixin.qq.com")
			.build();

		// 2. 创建适配器
		RestClientAdapter adapter = RestClientAdapter.create(restClient);

		// 3. 创建工厂并生成代理对象
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
		return factory.createClient(WorkWxBotApi.class);
	}
}
