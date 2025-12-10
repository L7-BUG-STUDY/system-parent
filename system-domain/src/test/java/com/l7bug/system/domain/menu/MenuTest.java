package com.l7bug.system.domain.menu;

import com.l7bug.common.error.ClientErrorCode;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {
	private MenuGateway menuGateway;
	private Menu system;
	private Menu menu;

	@BeforeEach
	void setUp() {
		menuGateway = Mockito.mock(MenuGateway.class);
		menu = new Menu(menuGateway);
		menu.setId((long) UUID.randomUUID().hashCode());
		menu.setFatherId(Menu.ROOT_ID);
		menu.setName(UUID.randomUUID().toString());
		menu.setFatherId(Menu.ROOT_ID);
		menu.setFullId("/" + menu.getId());

		system = new Menu(menuGateway);
		system.setId((long) UUID.randomUUID().hashCode());
		system.setName("system");
		system.setFatherId(Menu.ROOT_ID);
		system.setFullId("/" + system.getId());
		Mockito.doReturn(system).when(menuGateway).findById(system.getId());
		Mockito.doReturn(menu).when(menuGateway).findById(menu.getId());
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void addChildren() {
		menu.addChildren(null);
		menu.addChildren(new Menu(menuGateway));
	}

	@Test
	void moveFather() {
		menu.moveFather(123L);
		// 没有父节点的情况下,自己会成为根节点
		assertThat(menu)
			.as("没有父节点的情况下,自己会成为根节点,参数:[父节点id:%s,全路径id:%s]", menu.getFatherId(), menu.getFullId())
			.extracting(Menu::getFatherId, Menu::getFullId)
			.containsExactly(Menu.ROOT_ID, "/" + menu.getId());
		// 有父节点的话,自己会成为父节点下的一个子节点
		Mockito.doReturn(new ArrayList<>(Collections.singletonList(new Menu(menuGateway)))).when(menuGateway).findLikeFullId(Mockito.anyString());
		menu.moveFather(system.getId());
		assertThat(menu)
			.as("有父节点的话,自己会成为父节点下的一个子节点")
			.extracting(Menu::getFatherId, Menu::getFullId)
			.containsExactly(system.getId(), system.getFullId() + Menu.PATH_SEPARATOR + menu.getId());
	}

	@Test
	void save() {
		menu.save();
		new Menu(menuGateway).save();
		Menu menu1 = new Menu(menuGateway);
		menu1.setId((long) UUID.randomUUID().hashCode());
		menu1.setFatherId(system.getId());
		menu1.save();
		assertThat(menu1)
			.extracting(Menu::getFullId)
			.as("全路径id不能为空")
			.isNotNull()
			.as("验证全路径id是否在且挂在[system]节点下")
			.isEqualTo(system.getFullId() + Menu.PATH_SEPARATOR + menu1.getId());
		Menu updateFatherId = new Menu(menuGateway);
		updateFatherId.setId(menu.getId());
		updateFatherId.setFatherId(system.getId());
		updateFatherId.save();
		assertThat(updateFatherId)
			.extracting(Menu::getFullId)
			.as("全路径id不能为空")
			.isNotNull()
			.as("验证[menu]节点的全路径id是否在且挂在[system]节点下")
			.isEqualTo(system.getFullId() + Menu.PATH_SEPARATOR + updateFatherId.getId());
	}

	@Test
	void delete() {
		menu.delete();
		Mockito.doReturn(Collections.singletonList(new Menu(menuGateway))).when(menuGateway).findLikeFullId(Mockito.anyString());
		assertThatThrownBy(menu::delete)
			.isInstanceOf(Exception.class)
			.hasMessageContaining(ClientErrorCode.CHILDREN_IS_NOT_NULL.getMessage());
	}

	@Test
	void findChildrenTest() {
		Menu root = new Menu(menuGateway);
		root.setId((long) UUID.randomUUID().hashCode());
		root.setFatherId(Menu.ROOT_ID);
		root.setFullId(Menu.PATH_SEPARATOR + root.getId());
		Menu node01 = new Menu(menuGateway);
		node01.setId((long) UUID.randomUUID().hashCode());
		node01.setFatherId(root.getId());
		node01.setFullId(root.getFullId() + Menu.PATH_SEPARATOR + node01.getId());
		Menu node02 = new Menu(menuGateway);
		node02.setId((long) UUID.randomUUID().hashCode());
		node02.setFatherId(root.getId());
		node02.setFullId(root.getFullId() + Menu.PATH_SEPARATOR + node02.getId());

		Menu node02_01 = new Menu(menuGateway);
		node02_01.setId((long) UUID.randomUUID().hashCode());
		node02_01.setFatherId(node02.getId());
		node02_01.setFullId(node02.getFullId() + Menu.PATH_SEPARATOR + node02_01.getId());

		Menu node01_01 = new Menu(menuGateway);
		node01_01.setId((long) UUID.randomUUID().hashCode());
		node01_01.setFatherId(node01.getId());
		node01_01.setFullId(node01.getFullId() + Menu.PATH_SEPARATOR + node01_01.getId());
		root.findChildren();
		assertThat(root)
			.extracting(Menu::getChildren)
			.asInstanceOf(InstanceOfAssertFactories.LIST)
			.hasSize(0)
			.isEmpty();
		Mockito.doReturn(List.of(node01, node02, node02_01, node01_01)).when(menuGateway).findLikeFullId(root.getFullId());
		root.findChildren();
		List<Menu> children = root.getChildren();
		for (Menu child : children) {
			assertThat(child)
				.extracting(Menu::getId)
				.isIn(node01.getId(), node02.getId());
			assertThat(child.getChildren())
				.hasSize(1);
		}
		System.err.println(root);
	}
}
