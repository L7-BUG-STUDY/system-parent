package com.l7bug.system.convertor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapstructTest {
	@Autowired
	private UserMapstruct userMapstruct;

	@Test
	void createUser() {
		System.err.println(userMapstruct.createUser());
	}
}
