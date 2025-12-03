package com.l7bug.system.handler;


import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuthorizationDeniedException.class)
	public Result<Void> authorizationDenied(HttpServletRequest request, AuthorizationDeniedException ex) {
		log.warn("[{}-{}] 捕获未授权异常:[{}]", request.getMethod(), request.getRequestURL().toString(), ex.getMessage());
		return Results.failure(ClientErrorCode.ACCESS_DENIED);
	}

	@ExceptionHandler(AuthenticationServiceException.class)
	public Result<Void> authenticationException(AuthenticationServiceException ex) {
		log.warn("捕获登录账号或密码有误", ex);
		return Results.failure(ClientErrorCode.LOGIN_ERROR);
	}

	@ExceptionHandler(DisabledException.class)
	public Result<Void> internalAuthenticationServiceException(DisabledException ex) {
		log.warn("捕获用户被禁用异常", ex);
		return Results.failure(ClientErrorCode.USER_IS_DISABLE);
	}
}
