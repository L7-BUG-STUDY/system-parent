package com.l7bug.system.domain.role;

import com.l7bug.common.exception.ClientException;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

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

	@Test
	void save2() {
		Map<Long, Role> map = new HashMap<>();
		Mockito.doAnswer(invocation -> {
			Collection<Role> argument = invocation.getArgument(0);
			for (Role item : argument) {
				if (item.getId() == null) {
					item.setId((long) UUID.randomUUID().hashCode());
				}
				map.put(item.getId(), item);
			}
			return true;
		}).when(mock).save(Mockito.anyCollection());
		Mockito.doAnswer(invocation -> {
			Role argument = invocation.getArgument(0);
			return mock.save(List.of(argument));
		}).when(mock).save(Mockito.any(Role.class));
		Mockito.doAnswer(invocation -> {
			Long argument = invocation.getArgument(0);
			return Optional.ofNullable(map.get(argument));
		}).when(mock).findById(Mockito.anyLong());
		Mockito.doAnswer(invocation -> {
			String argument = invocation.getArgument(0);
			return map.values().parallelStream().filter(item -> item.getFullCode().equals(argument)).findFirst();
		}).when(mock).findByFullCode(Mockito.anyString());
		Mockito.doAnswer(invocation -> {
			String argument = invocation.getArgument(0);
			return map.values().parallelStream().filter(item -> item.getFullCode().startsWith(argument)).toList();
		}).when(mock).findLikeFullCode(Mockito.anyString());

		role.setName("root");
		role.setCode(UUID.randomUUID().toString());
		role.enabled();
		role.setFatherFullCode(Role.PATH_SEPARATOR);
		role.save();
		Role node01 = new Role(mock);
		node01.setName(faker.name().fullName());
		node01.setCode(UUID.randomUUID().toString());
		node01.enabled();
		node01.setFatherFullCode(Role.PATH_SEPARATOR);
		node01.save();

		Role node02 = new Role(mock);
		node02.setName(faker.name().fullName());
		node02.setCode(UUID.randomUUID().toString());
		node02.enabled();
		node02.setFatherFullCode(Role.PATH_SEPARATOR);
		node02.save();

		Role node03 = new Role(mock);
		node03.setName(faker.name().fullName());
		node03.setCode(UUID.randomUUID().toString());
		node03.enabled();
		node03.setFatherFullCode(Role.PATH_SEPARATOR);
		node03.save();
		node03.setFatherFullCode(node02.getFullCode());
		node03.save();
		Assertions.assertThat(node03.getFullCode())
			.as("节点3应该在节点2下面")
			.startsWith(node02.getFullCode());
		node03.save();
		Role node04 = new Role(mock);
		node04.setCode(node03.getCode());
		node04.setFatherFullCode(node03.getFatherFullCode());
		Assertions.assertThatThrownBy(node04::save)
			.as("相同的全路径编码会报错")
			.isInstanceOf(ClientException.class)
			.isNotNull();
		Role node02Temp = new Role(mock);
		node02Temp.setId(node02.getId());
		node02Temp.setName(faker.name().fullName());
		node02Temp.setCode(node02.getCode());
		node02Temp.enabled();
		node02Temp.setFatherFullCode(node01.getFullCode());
		node02Temp.save();
		Optional<Role> byId = this.mock.findById(node02.getId());
		Assertions.assertThat(byId).isPresent();
		node02 = byId.get();
		Assertions.assertThat(node02.getFullCode())
			.as("修改父节点,连带着子节点都会调整")
			.startsWith(node01.getFullCode());
		Assertions.assertThat(node03.getFullCode())
			.as("修改父节点,连带着子节点都会调整")
			.startsWith(node01.getFullCode());
	}
}
