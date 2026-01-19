package com.l7bug.system.filter;

import com.l7bug.common.etc.Headers;
import com.l7bug.system.config.AuthConfiguration;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

@SpringBootTest
class JwtAuthenticationFilterTest {
	Faker faker = new Faker(Locale.CHINA);
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private UserGateway userGateway;
	@Autowired
	private AuthConfiguration.AuthProperties authProperties;

	@Test
	void doFilterInternal() throws ServletException, IOException {
		MDC.clear();
		User user = userGateway.getUserByUsername("root");
		user.setRawPassword("123456");
		HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

		Mockito.doReturn("/test/test/test").when(httpServletRequest).getRequestURI();
		// 测试没有凭证
		jwtAuthenticationFilter.doFilterInternal(httpServletRequest, Mockito.mock(HttpServletResponse.class), Mockito.mock(FilterChain.class));
		// 测试有token
		String login = user.login();
		Mockito.doReturn(login).when(httpServletRequest).getHeader(Headers.TOKEN);
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
