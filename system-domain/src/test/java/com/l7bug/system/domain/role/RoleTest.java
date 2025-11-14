package com.l7bug.system.domain.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleTest {
	private Role role;

	@BeforeEach
	void setUp() {
		role = new Role(Mockito.mock(RoleGateway.class));
		role.setStatus(RoleStatus.ENABLED);
		System.err.println(role);
	}

	@Test
	public void showRole() {
		System.err.println(role);
	}
}
