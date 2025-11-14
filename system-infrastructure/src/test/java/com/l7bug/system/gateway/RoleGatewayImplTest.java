package com.l7bug.system.gateway;

import com.l7bug.system.domain.role.Role;
import com.l7bug.system.domain.role.RoleGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class RoleGatewayImplTest {
	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void test() {
		Role bean1 = applicationContext.getBean(Role.class);
		Role bean2 = applicationContext.getBean(Role.class);
		Role bean3 = applicationContext.getBean(Role.class);
		Role bean4 = applicationContext.getBean(Role.class);
		Assertions.assertNotSame(bean1, bean2);
		Assertions.assertNotSame(bean3, bean4);
		Assertions.assertSame(applicationContext.getBean(RoleGateway.class), applicationContext.getBean(RoleGatewayImpl.class));
	}
}
