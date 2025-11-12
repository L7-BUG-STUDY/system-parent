package com.l7bug.system.config;

import com.l7bug.system.filter.JwtAuthenticationEntryPoint;
import com.l7bug.system.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * 认证配置
 *
 * @author l
 * @date 2022/4/10 下午12:11
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class AppSecurityConfiguration {
	public final static String LOGIN_URL = "/auth/login";
	private final AuthConfiguration.AuthProperties authProperties;

	private final JwtAuthenticationFilter jwtAuthorizationFilter;

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// 禁用 CSRF
			.csrf(AbstractHttpConfigurer::disable)

			// 配置 CORS
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))

			// 配置会话管理（无状态）
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			// 配置异常处理
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			)
			// 配置授权规则
			.authorizeHttpRequests(auth ->
				auth
					// 放行无需认证的URL
					.requestMatchers(authProperties.getWhiteApi()).permitAll()
					// 其他所有请求都需要认证
					.anyRequest().authenticated()
			)

			// 添加JWT过滤器
			.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * 配置CORS（跨域资源共享）
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
