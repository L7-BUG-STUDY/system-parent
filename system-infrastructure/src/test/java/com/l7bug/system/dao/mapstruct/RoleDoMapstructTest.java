package com.l7bug.system.dao.mapstruct;

import com.l7bug.system.domain.role.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleDoMapstructTest {
	@Autowired
	private RoleDoMapstruct roleDoMapstruct;

	@Test
	void role() {
		Role role = roleDoMapstruct.role();
		Assertions.assertThat(role).isNotNull();
	}
}
