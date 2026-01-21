package com.l7bug.system.domain.role;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Locale;
import java.util.PriorityQueue;

class RoleTest {
	private final Faker faker = new Faker(Locale.CHINA);
	private RoleGateway mock;
	private Role role;

	@BeforeEach
	void setUp() {
		mock = Mockito.mock();
		role = new Role(mock);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void disabled() {
		role.disabled();
		Assertions.assertThat(role.getStatus()).isEqualTo(RoleStatus.DISABLED);
	}

	@Test
	void enabled() {
		role.enabled();
		Assertions.assertThat(role.getStatus()).isEqualTo(RoleStatus.ENABLED);
	}

	@Test
	void save() {
		role.save();
	}

	@Test
	void compareTo() {
		Role role1 = new Role(mock);
		Role role2 = new Role(mock);
		role1.setId(1L);
		role2.setId(2L);
		var roles = new PriorityQueue<Role>();
		roles.add(role1);
		roles.add(role2);
		Role poll = roles.poll();
		Assertions.assertThat(poll).isNotNull().as("判断排序号相同,id不同,使用id作为排序条件").isSameAs(role1);
		role2.setSort(-1);
		roles = new PriorityQueue<Role>();
		roles.add(role1);
		roles.add(role2);
		poll = roles.poll();
		Assertions.assertThat(poll).isNotNull().as("判断排序号不相同,id不同,使用排序号作为排序条件").isSameAs(role2);

		role1.setSort(0);
		role2.setSort(0);

		role1.setId(null);
		roles = new PriorityQueue<>();
		roles.add(role1);
		roles.add(role2);
		poll = roles.poll();
		Assertions.assertThat(poll).isNotNull().as("判断排序号不相同,id为null,拥有id的在前面").isSameAs(role2);
		role1.setId(1L);
		role2.setId(null);
		roles = new PriorityQueue<>();
		roles.add(role1);
		roles.add(role2);
		poll = roles.poll();
		Assertions.assertThat(poll).isNotNull().as("判断排序号不相同,id为null,拥有id的在前面").isSameAs(role1);
	}

	@Test
	void delete() {
		this.role.delete();
		this.role.setId(1L);
		this.role.delete();
	}
}
