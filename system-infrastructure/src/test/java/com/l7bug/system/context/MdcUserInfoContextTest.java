package com.l7bug.system.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class MdcUserInfoContextTest {
	@Test
	void getMdcUserName() {
		String string = UUID.randomUUID().toString();
		MdcUserInfoContext.putMdcUserName(string);
		Assertions.assertEquals(string, MdcUserInfoContext.getMdcUserName());
	}

	@Test
	void getMdcTraceId() {
		String string = UUID.randomUUID().toString();
		MdcUserInfoContext.putMdcTraceId(string);
		Assertions.assertEquals(string, MdcUserInfoContext.getMdcTraceId());
	}

	@Test
	void getMdcToken() {
		String string = UUID.randomUUID().toString();
		MdcUserInfoContext.putMdcToken(string);
		Assertions.assertEquals(string, MdcUserInfoContext.getMdcToken());
	}
}
