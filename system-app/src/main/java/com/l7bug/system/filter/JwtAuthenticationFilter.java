package com.l7bug.system.filter;

import cn.hutool.core.bean.BeanUtil;
import com.l7bug.system.context.MdcUserInfoContext;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.etc.SystemEtc;
import com.l7bug.system.security.UserDetailsImpl;
import com.l7bug.system.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter
 *
 * @author l
 * @since 2025/11/1 20:26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final TokenService tokenService;

	private final UserGateway userGateway;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String token = request.getHeader(SystemEtc.TOKEN_HEADER);
		if (SecurityContextHolder.getContext().getAuthentication() == null && tokenService.tokenValid(token)) {
			MdcUserInfoContext.putMdcToken(token);
			tokenService.expireToken(token);
			User user = userGateway.currentUser();
			MdcUserInfoContext.putMdcUserName(user.getUsername());
			MdcUserInfoContext.putMdcNickname(user.getNickname());
			MdcUserInfoContext.putUserId(user.getId().toString());
			UserDetailsImpl userDetails = BeanUtil.copyProperties(user, UserDetailsImpl.class);
			userDetails.setPassword("123456");
			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			// authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
}
