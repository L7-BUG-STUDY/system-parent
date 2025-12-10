package com.l7bug.system.gateway;

import com.github.javafaker.Faker;
import com.l7bug.common.exception.ClientException;
import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.user.User;
import com.l7bug.web.context.MdcUserInfoContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@SpringBootTest
class MenuGatewayImplTest {
	private final Faker faker = new Faker(Locale.CHINA);
	@Autowired
	private MenuGatewayImpl menuGateway;
	@Autowired
	private UserGatewayImpl userGateway;

	@BeforeEach
	void setUp() {
		User root = userGateway.getUserById(-1L);
		root.setRawPassword("123456");
		String login = root.login();
		MdcUserInfoContext.putMdcToken(login);
		MdcUserInfoContext.putMdcTraceId(UUID.randomUUID().toString());
		MdcUserInfoContext.putMdcUserName(root.getUsername());
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void appendChildrenTest() {
		List<Menu> byFullId = this.menuGateway.findLikeFullId(null);
		Assertions.assertTrue(byFullId.isEmpty());
		Menu root = new Menu(menuGateway);
		root.setName(faker.name().fullName());
		root.save();
		Menu children = new Menu(menuGateway);
		children.setName(faker.name().fullName());
		root.addChildren(children);
		byFullId = this.menuGateway.findLikeFullId(root.getFullId());
		Assertions.assertEquals(1, byFullId.size());
		Assertions.assertEquals(children.getId(), byFullId.get(0).getId());
		Assertions.assertThrows(ClientException.class, root::delete);

		Menu root2 = new Menu(menuGateway);
		root2.setName(faker.name().fullName());
		root2.save();
		// 修改父节点
		children.moveFather(root2.getId());
		// 验证是否修改成功
		Assertions.assertTrue(this.menuGateway.findLikeFullId(root.getFullId()).isEmpty());
		byFullId = this.menuGateway.findLikeFullId(root2.getFullId());
		Assertions.assertFalse(byFullId.isEmpty());
		Assertions.assertEquals(root2.getId(), byFullId.get(0).getFatherId());
		Assertions.assertEquals(children.getId(), byFullId.get(0).getId());
		// 测试删除操作
		children.delete();
		root.delete();
		root2.delete();
		Assertions.assertNull(this.menuGateway.findById(root.getId()));
		Assertions.assertNull(this.menuGateway.findById(children.getId()));
		Assertions.assertTrue(this.menuGateway.findLikeFullId(root.getFullId()).isEmpty());
	}

	@Test
	void save() {
		List<Menu> menus = List.of(new Menu(menuGateway), new Menu(menuGateway));
		for (Menu menu : menus) {
			menu.setName(faker.name().name());
			menu.save();
			Assertions.assertEquals(Menu.ROOT_ID, menu.getFatherId());
			Assertions.assertEquals(Menu.PATH_SEPARATOR + menu.getId(), menu.getFullId());
			Menu byId = this.menuGateway.findById(menu.getId());
			Assertions.assertNotNull(byId);
			Assertions.assertEquals(Menu.ROOT_ID, byId.getFatherId());
			Assertions.assertEquals(byId.getFullId(), menu.getFullId());
			byId.save();
			byId.delete();
		}
	}

	@Test
	void findAllRootNodeTest() {
		Menu menu = new Menu(menuGateway);
		menu.setName(faker.name().fullName());
		menu.save();
		Menu menu2 = new Menu(menuGateway);
		menu2.setName(faker.name().fullName());
		menu2.save();
		List<Menu> allRootNode = this.menuGateway.findAllRootNode();
		boolean flag = allRootNode.parallelStream().map(Menu::getId).anyMatch(menu.getId()::equals);
		boolean flag2 = allRootNode.parallelStream().map(Menu::getId).anyMatch(menu2.getId()::equals);
		Assertions.assertTrue(flag);
		Assertions.assertTrue(flag2);
		menu.delete();
		menu2.delete();
	}
}
