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
}
