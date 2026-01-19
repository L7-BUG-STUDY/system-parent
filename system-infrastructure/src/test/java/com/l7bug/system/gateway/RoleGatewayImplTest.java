package com.l7bug.system.gateway;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import com.l7bug.system.domain.role.Role;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@SpringBootTest
class RoleGatewayImplTest {
	private final Faker faker = new Faker(Locale.CHINA);
	@Autowired
	private RoleGatewayImpl roleGateway;
	private Role role;

	@BeforeEach
	void setUp() {
		role = new Role(roleGateway);
		role.setName(faker.name().fullName());
		role.setCode(UUID.randomUUID().toString());
		role.enabled();
		role.setFatherFullCode(Role.PATH_SEPARATOR);
		role.setFullCode(Role.PATH_SEPARATOR + role.getCode());
	}

	@Test
	void save() {

		Assertions.assertThatThrownBy(new Role(roleGateway)::save)
			.isNotNull()
			.satisfies(temp -> log.info("测试参数校验::异常::{}", temp))
			.message()
			.isNotBlank()
			.contains("父级编码不能为空", "角色名称不能为空", "角色编码不能为空");
		role.save();
		Assertions.assertThat(role.getId())
			.isNotNull();
		role.delete();
	}

	@Test
	void findLikeFullCode() {
		role.save();
		Assertions.assertThat(roleGateway.findLikeFullCode(role.getFullCode()))
			.isNotNull()
			.hasSize(1)
			.first()
			.extracting(Role::getId)
			.isEqualTo(role.getId());
		Role role = new Role(roleGateway);
		role.setName(faker.name().fullName());
		role.setCode(UUID.randomUUID().toString());
		role.enabled();
		role.setFatherFullCode(this.role.getFullCode());
		role.setFullCode(this.role.getFullCode() + Role.PATH_SEPARATOR + role.getCode());
		role.save();
		Assertions.assertThat(roleGateway.findLikeFullCode(this.role.getFullCode()))
			.isNotNull()
			.hasSize(2)
			.extracting(Role::getId)
			.contains(role.getId(), this.role.getId());
		this.role.delete();
		role.delete();
	}

	@Test
	void findById() {
		role.save();
		if (role.getId() == null) {
			return;
		}
		Assertions.assertThat(roleGateway.findById(role.getId()))
			.isNotNull()
			.satisfies(temp -> log.info("测试查询::{}", temp))
			.get()
			.extracting(Role::getId)
			.isEqualTo(role.getId());
		role.delete();
	}

	@Test
	void deleteById() {
		role.save();
		role.delete();
		if (role.getId() == null) {
			return;
		}
		Assertions.assertThat(roleGateway.findById(role.getId()))
			.isEmpty();
	}

	@Test
	void findByFullCode() {
		role.save();
		Assertions.assertThat(this.roleGateway.findByFullCode(role.getFullCode()))
			.isPresent()
			.get()
			.extracting(Role::getId)
			.isEqualTo(role.getId());
		Role role = new Role(roleGateway);
		role.setName(faker.name().fullName());
		role.setCode(this.role.getCode());
		role.enabled();
		role.setFatherFullCode(Role.PATH_SEPARATOR);
		role.setFullCode(Role.PATH_SEPARATOR + role.getCode());
		Assertions.assertThatThrownBy(role::save)
			.isNotNull()
			.isInstanceOf(ClientException.class)
			.message()
			.isNotBlank()
			.satisfies(log::error)
			.contains(ClientErrorCode.NODE_IS_NOT_NULL.getMessage());
		this.role.delete();
	}

	@Test
	void testSave() {
		Role role = new Role(roleGateway);
		role.setName(faker.name().fullName());
		role.setCode(UUID.randomUUID().toString());
		role.enabled();
		role.setFatherFullCode(Role.PATH_SEPARATOR);
		role.setFullCode(Role.PATH_SEPARATOR + role.getCode());
		Assertions.assertThat(this.roleGateway.save(List.of(role, this.role)))
			.isTrue();
		Assertions.assertThat(role.getId())
			.isNotNull();
		Assertions.assertThat(this.role.getId())
			.isNotNull()
			.isNotSameAs(role.getId());
	}
}
