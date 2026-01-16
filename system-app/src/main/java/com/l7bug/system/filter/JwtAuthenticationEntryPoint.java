package com.l7bug.system.filter;

import com.alibaba.fastjson2.JSON;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.result.Results;
import com.l7bug.web.context.MdcUserInfoContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * AccessDeniedHandler
 *
 * @author l
 * @since 2025/11/1 22:34
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		log.warn("[{}]::未认证拦截器捕获!", request.getRequestURI());
		log.warn("e:", authException);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(JSON.toJSONString(Results.buildResult(MdcUserInfoContext.getMdcTraceId(), ClientErrorCode.NOT_AUTHENTICATION.getCode(), ClientErrorCode.NOT_AUTHENTICATION.getMessage(), null)));
	}
}
