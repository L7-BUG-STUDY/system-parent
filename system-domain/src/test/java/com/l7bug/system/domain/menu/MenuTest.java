package com.l7bug.system.domain.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

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
		Assertions.assertEquals(Menu.ROOT_ID, menu.getFatherId());
		Assertions.assertEquals("/" + menu.getId(), menu.getFullId());
		// 有父节点的话,自己会成为父节点下的一个子节点
		Mockito.doReturn(new ArrayList<>(Collections.singletonList(new Menu(menuGateway)))).when(menuGateway).findByFullId(Mockito.anyString());
		menu.moveFather(system.getId());
		Assertions.assertEquals(system.getId(), menu.getFatherId());
		Assertions.assertEquals(system.getFullId() + "/" + menu.getId(), menu.getFullId());
	}

	@Test
	void save() {
		menu.save();
		new Menu(menuGateway).save();
		Menu menu1 = new Menu(menuGateway);
		menu1.setId(menu.getId());
		menu1.setFatherId(system.getId());
		menu1.save();
	}

	@Test
	void delete() {
		menu.delete();
		Mockito.doReturn(Collections.singletonList(new Menu(menuGateway))).when(menuGateway).findByFullId(Mockito.anyString());
		Assertions.assertThrows(Exception.class, system::delete);
	}
}
