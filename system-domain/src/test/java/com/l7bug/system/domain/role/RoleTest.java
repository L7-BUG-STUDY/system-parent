package com.l7bug.system.domain.role;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleTest {
	private Role role;
	private Faker faker = new Faker();

	@BeforeEach
	void setUp() {
		role = new Role(Mockito.mock(RoleGateway.class));
		role.setStatus(RoleStatus.ENABLE);
		System.err.println(role);
	}

	@Test
	public void showRole() {
		System.err.println(role);
	}
}
