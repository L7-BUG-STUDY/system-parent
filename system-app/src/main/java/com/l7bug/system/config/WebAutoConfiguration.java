package com.l7bug.system.config;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.l7bug.common.result.Result;
import com.l7bug.system.context.MdcUserInfoContext;
import com.l7bug.system.etc.SystemEtc;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebAutoConfiguration
 *
 * @author Administrator
 * @since 2025/11/4 17:52
 */
@Slf4j
@Configuration
public class WebAutoConfiguration implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
				@Override
				public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
					String requestId = request.getHeader(SystemEtc.REQUEST_ID);
					String token = request.getHeader(SystemEtc.TOKEN_HEADER);
					if (StrUtil.isBlank(requestId)) {
						requestId = IdUtil.getSnowflakeNextIdStr();
					}
					MdcUserInfoContext.putMdcRequestId(requestId);
					if (StrUtil.isNotBlank(token)) {
						MdcUserInfoContext.putMdcToken(token);
					}
					log.info("开始请求:{}", request.getRequestURI());
					long currentTimeMillis = System.currentTimeMillis();
					MDC.put("currentTimeMillis", String.valueOf(currentTimeMillis));
					return HandlerInterceptor.super.preHandle(request, response, handler);
				}

				@Override
				public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
					String s = MDC.get("currentTimeMillis");
					log.info("请求耗时:{}/ms", System.currentTimeMillis() - Long.parseLong(s));
					HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
				}
			})
			.order(Integer.MIN_VALUE)
			.addPathPatterns("/**");
	}

	@Bean
	public SetRequestAspect setRequestAspect() {
		return new SetRequestAspect();
	}

	/**
	 * WebAutoConfiguration
	 *
	 * @author Administrator
	 * @since 2025/11/4 17:52
	 */
	@Aspect
	public static class SetRequestAspect {

		@AfterReturning(value = "execution(public com.l7bug.common.result.Result *(..))", returning = "result")
		public void afterReturning(Result<?> result) {
			result.setRequestId(MdcUserInfoContext.getMdcRequestId());
		}
	}
}
