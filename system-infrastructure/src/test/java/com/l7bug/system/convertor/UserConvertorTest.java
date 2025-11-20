package com.l7bug.system.convertor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserConvertorTest {
	UserConvertor userConvertor;

	@BeforeEach
	void setUp() {
		userConvertor = new UserConvertor(null);
	}

	@Test
	void mapDomain() {
	}

	@Test
	void mapDo() {
		userConvertor.mapDo(null);
	}
}
