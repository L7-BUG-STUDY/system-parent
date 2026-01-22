package com.l7bug.system.gateway;

import cn.hutool.core.util.IdUtil;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

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
		role.enabled();
		role.setFatherId(Role.ROOT_ID);
	}

	@Test
	void save() {
		Assertions.assertThatThrownBy(new Role(roleGateway)::save)
			.isNotNull()
			.satisfies(temp -> log.info("测试参数校验::异常::{}", temp))
			.message()
			.isNotBlank()
			.contains("角色名称不能为空");
		role.save();
		Assertions.assertThat(role.getId())
			.isNotNull();
		Assertions.assertThat(role)
			.extracting(Role::getId, Role::getName, Role::getFatherId, Role::getFullId)
			.isNotNull()
			.isNotEmpty()
		;
		role.delete();
	}

	@Test
	void findLikeRightFullId() {
		role.save();
		Assertions.assertThat(role.getId()).isNotNull();
		Assertions.assertThat(roleGateway.findLikeRightFullId(role.getFullId()))
			.isNotNull()
			.hasSize(1)
			.first()
			.extracting(Role::getId)
			.isEqualTo(role.getId());
		Role role = new Role(roleGateway);
		role.setName(faker.name().fullName());
		role.enabled();
		role.setFatherId(this.role.getId());
		role.save();
		Assertions.assertThat(roleGateway.findLikeRightFullId(this.role.getFullId()))
			.isNotNull()
			.hasSize(2)
			.extracting(Role::getId)
			.contains(role.getId(), this.role.getId());
		role.delete();
		this.role.delete();
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
		Assertions.assertThat(roleGateway.findById(null))
			.isNotPresent();
	}

	@Test
	void deleteById() {
		this.roleGateway.deleteById(null);
		role.save();
		Role role1 = new Role(roleGateway);
		role1.setName(faker.name().fullName());
		Assertions.assertThat(role.getId()).isNotNull();
		role1.setFatherId(role.getId());
		role1.enabled();
		role1.save();
		Assertions.assertThatThrownBy(role::delete)
			.isNotNull()
			.isInstanceOf(ClientException.class)
			.satisfies(temp -> log.info("测试删除::异常::{}", temp))
			.message()
			.contains(ClientErrorCode.CHILDREN_IS_NOT_NULL.getMessage())
		;
		role1.delete();
		role.delete();
		if (role.getId() == null) {
			return;
		}
		Assertions.assertThat(roleGateway.findById(role.getId()))
			.isEmpty();
	}

	@Test
	void testSave() {
		Assertions.assertThat(this.roleGateway.save(List.of()))
			.isFalse();
		this.role.save();
		Role role = new Role(roleGateway);
		role.setName(faker.name().fullName());
		role.enabled();
		role.setFatherId(Role.ROOT_ID);
		role.save();
		Assertions.assertThat(this.roleGateway.save(List.of(role, this.role)))
			.isTrue();
		Assertions.assertThat(role.getId())
			.isNotNull();
		Assertions.assertThat(this.role.getId())
			.isNotNull()
			.isNotSameAs(role.getId());
		role.delete();
		this.role.delete();
	}

	@Test
	void findByFatherId() {
		role.save();
		Assertions.assertThat(role.getId()).isNotNull();
		Role role1 = new Role(roleGateway);
		role1.setName(faker.name().fullName());
		role1.setFatherId(role.getId());
		role1.save();
		Assertions.assertThat(roleGateway.findByFatherId(role.getId()))
			.isNotNull()
			.hasSize(1)
			.first()
			.extracting(Role::getId)
			.isEqualTo(role1.getId());
	}

	@Transactional
	@Test
	void assignMenus() {
		role.save();
		Assertions.assertThat(role.getId()).isNotNull();
		this.roleGateway.assignMenus(role.getId(), List.of(IdUtil.getSnowflakeNextId(), IdUtil.getSnowflakeNextId(), IdUtil.getSnowflakeNextId()));
		List<Long> menuIds = this.roleGateway.findMenuIds(role.getId());
		Assertions.assertThat(menuIds)
			.isNotNull()
			.hasSize(3)
		;
		this.roleGateway.deleteMenusByRoleId(role.getId());
		role.delete();
	}

	@Transactional
	@Test
	void deleteMenusByRoleId() {
		role.save();
		Assertions.assertThat(role.getId()).isNotNull();
		this.roleGateway.assignMenus(role.getId(), List.of(IdUtil.getSnowflakeNextId(), IdUtil.getSnowflakeNextId(), IdUtil.getSnowflakeNextId()));
		this.roleGateway.deleteMenusByRoleId(role.getId());
		Assertions.assertThat(this.roleGateway.findMenuIds(role.getId()))
			.isEmpty();
		role.delete();
	}
}
