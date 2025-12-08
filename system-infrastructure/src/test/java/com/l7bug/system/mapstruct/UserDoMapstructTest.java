package com.l7bug.system.mapstruct;

import com.l7bug.system.domain.user.User;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserDoMapstructTest {
	@Autowired
	private UserDoMapstruct userDoMapstruct;

	@Test
	void mapDomain() {
		User user = userDoMapstruct.mapDomain(null);
		Assertions.assertNull(user);
		SystemUser user1 = new SystemUser();
		user1.setStatus(null);
		user = userDoMapstruct.mapDomain(user1);
		Assertions.assertNotNull(user);
		Assertions.assertNull(user.getStatus());
	}

	@Test
	void mapDo() {
		SystemUser systemUser = userDoMapstruct.mapDo(null);
		Assertions.assertNull(systemUser);
		User user = new User(null);
		user.setStatus(null);
		systemUser = userDoMapstruct.mapDo(user);
		Assertions.assertNotNull(systemUser);
		Assertions.assertNull(systemUser.getStatus());
	}
}
