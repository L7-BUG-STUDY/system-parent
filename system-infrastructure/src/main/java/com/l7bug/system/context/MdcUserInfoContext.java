package com.l7bug.system.context;

import org.slf4j.MDC;

import java.util.Optional;

/**
 * TokenContext
 *
 * @author Administrator
 * @since 2025/11/10 15:13
 */
public class MdcUserInfoContext {
	private final static String MDC_USER_ID = "userId";

	private final static String MDC_USER_NAME = "username";
	private final static String MDC_NICKNAME = "nickname";
	private final static String MDC_REQUEST_ID = "requestId";
	private final static String MDC_TOKEN = "token";

	public static Long userId() {
		try {
			return Optional.ofNullable(MDC.get(MDC_USER_ID)).map(Long::valueOf).orElse(-1L);

		} catch (IllegalArgumentException e) {
			return -1L;
		}
	}

	public static void putUserId(String userId) {
		MDC.put(MDC_USER_ID, userId);
	}

	public static void putRequestId(String requestId) {
		MDC.put("requestId", requestId);
	}

	public static void putMdcUserName(String mdcUserName) {
		MDC.put(MDC_USER_NAME, mdcUserName);
	}

	public static String getMdcUserName() {
		return Optional.ofNullable(MDC.get(MDC_USER_NAME)).orElse("");
	}

	public static void putMdcNickname(String mdcNickname) {
		MDC.put(MDC_NICKNAME, mdcNickname);
	}

	public static String getMdcNickname() {
		return Optional.ofNullable(MDC.get(MDC_NICKNAME)).orElse("");
	}

	public static void putMdcRequestId(String mdcRequestId) {
		MDC.put(MDC_REQUEST_ID, mdcRequestId);
	}

	public static String getMdcRequestId() {
		return Optional.ofNullable(MDC.get(MDC_REQUEST_ID)).orElse("");
	}

	public static void putMdcToken(String token) {
		MDC.put(MDC_TOKEN, token);
	}

	public static String getMdcToken() {
		return Optional.ofNullable(MDC.get(MDC_TOKEN)).orElse("");
	}
}
