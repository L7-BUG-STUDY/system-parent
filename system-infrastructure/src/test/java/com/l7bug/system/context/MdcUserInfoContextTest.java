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
	void getMdcRequestId() {
		String string = UUID.randomUUID().toString();
		MdcUserInfoContext.putMdcRequestId(string);
		Assertions.assertEquals(string, MdcUserInfoContext.getMdcRequestId());
	}

	@Test
	void getMdcToken() {
		String string = UUID.randomUUID().toString();
		MdcUserInfoContext.putMdcToken(string);
		Assertions.assertEquals(string, MdcUserInfoContext.getMdcToken());
	}
}
