package com.l7bug.system.filter;

import com.github.javafaker.Faker;
import com.l7bug.system.config.AuthConfiguration;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.etc.SystemEtc;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

@SpringBootTest
class JwtAuthenticationFilterTest {
	Faker faker = new Faker(Locale.CANADA);
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private UserGateway userGateway;
	@Autowired
	private AuthConfiguration.AuthProperties authProperties;

	@Test
	void doFilterInternal() throws ServletException, IOException {
		User user = new User(userGateway);
		user.setEnable();
		user.setNickname(faker.name().name());
		user.setUsername(UUID.randomUUID().toString().replace("-", ""));
		user.setRawPassword(faker.phoneNumber().cellPhone());
		user.save();
		HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

		Mockito.doReturn("/test/test/test").when(httpServletRequest).getRequestURI();
		// 测试没有凭证
		jwtAuthenticationFilter.doFilterInternal(httpServletRequest, Mockito.mock(HttpServletResponse.class), Mockito.mock(FilterChain.class));
		// 测试有token
		String login = user.login();
		Mockito.doReturn(login).when(httpServletRequest).getHeader(SystemEtc.TOKEN_HEADER);
		jwtAuthenticationFilter.doFilterInternal(httpServletRequest, Mockito.mock(HttpServletResponse.class), Mockito.mock(FilterChain.class));
		// 测试有凭证
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken("userDetails", null, Collections.emptyList());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		jwtAuthenticationFilter.doFilterInternal(httpServletRequest, Mockito.mock(HttpServletResponse.class), Mockito.mock(FilterChain.class));
		Mockito.doReturn(authProperties.getWhiteApi()[0]).when(httpServletRequest).getRequestURI();
		jwtAuthenticationFilter.doFilterInternal(httpServletRequest, Mockito.mock(HttpServletResponse.class), Mockito.mock(FilterChain.class));

	}
}
