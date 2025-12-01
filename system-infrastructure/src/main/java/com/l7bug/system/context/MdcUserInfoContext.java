package com.l7bug.system.context;

import org.slf4j.MDC;

import java.util.Optional;

/**
 * TokenContext
 *
 * @author Administrator
 * @since 2025/11/10 15:13
 */
public final class MdcUserInfoContext {

	private final static String MDC_USER_NAME = "username";
	private final static String MDC_TRACE_ID = "traceId";
	private final static String MDC_TOKEN = "token";

	private MdcUserInfoContext() {
	}

	public static void putMdcTraceId(String requestId) {
		MDC.put(MDC_TRACE_ID, requestId);
	}

	public static void putMdcUserName(String mdcUserName) {
		MDC.put(MDC_USER_NAME, mdcUserName);
	}

	public static String getMdcUserName() {
		return Optional.ofNullable(MDC.get(MDC_USER_NAME)).orElse("");
	}

	public static String getMdcTraceId() {
		return Optional.ofNullable(MDC.get(MDC_TRACE_ID)).orElse("");
	}

	public static void putMdcToken(String token) {
		MDC.put(MDC_TOKEN, token);
	}

	public static String getMdcToken() {
		return Optional.ofNullable(MDC.get(MDC_TOKEN)).orElse("");
	}
}
