package com.l7bug.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AuthConfig
 *
 * @author Administrator
 * @since 2025/11/12 15:46
 */
@EnableConfigurationProperties(AuthConfiguration.AuthProperties.class)
@Configuration
public class AuthConfiguration {
	@ConfigurationProperties(prefix = "l7bug.auth")
	@Data
	public static class AuthProperties {
		private String[] whiteApi = {"/auth/login"};
	}
}
