package com.l7bug.system.domain.menu;

import com.l7bug.common.error.ClientErrorCode;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 菜单领域对象测试类
 * <p>
 * 包含对Menu领域对象各种操作的测试，如添加子菜单、移动父节点、保存、删除等操作
 */
class MenuTest {
	private MenuGateway menuGateway;
	private Menu system;
	private Menu menu;

	/**
	 * 在每个测试方法执行前进行初始化工作
	 * 创建mock的MenuGateway对象以及测试用的Menu对象
	 */
	@BeforeEach
	void setUp() {
		// 创建mock的菜单网关对象
		menuGateway = Mockito.mock(MenuGateway.class);

		// 初始化测试用的菜单对象
		menu = new Menu(menuGateway);
		menu.setId((long) UUID.randomUUID().hashCode());
		menu.setFatherId(Menu.ROOT_ID);
		menu.setName(UUID.randomUUID().toString());
		menu.setFatherId(Menu.ROOT_ID);
		menu.setFullId("/" + menu.getId());

		// 初始化系统菜单对象
		system = new Menu(menuGateway);
		system.setId((long) UUID.randomUUID().hashCode());
		system.setName("system");
		system.setFatherId(Menu.ROOT_ID);
		system.setFullId("/" + system.getId());

		// 配置mock对象的预期行为
		Mockito.doReturn(system).when(menuGateway).findById(system.getId());
		Mockito.doReturn(menu).when(menuGateway).findById(menu.getId());
	}

	/**
	 * 在每个测试方法执行后进行清理工作
	 */
	@AfterEach
	void tearDown() {
		// 清理资源的代码可以在这里添加
	}

	/**
	 * 测试添加子菜单功能
	 * 验证添加null子菜单不会出错，以及正常添加子菜单的功能
	 */
	@Test
	void addChildren() {
		// 测试添加null子菜单的情况
		menu.addChildren(null);
		// 测试添加正常的子菜单
		menu.addChildren(new Menu(menuGateway));
	}

	/**
	 * 测试移动父节点功能
	 * 验证菜单在不同情况下的父节点变更行为：
	 * 1. 当没有指定有效父节点时，菜单应成为根节点
	 * 2. 当指定有效父节点时，菜单应成为该父节点的子节点
	 */
	@Test
	void moveFather() {
		// 移动到不存在的父节点，应该成为根节点
		menu.moveFather(123L);
		// 没有父节点的情况下,自己会成为根节点
		assertThat(menu)
			.as("没有父节点的情况下,自己会成为根节点,参数:[父节点id:%s,全路径id:%s]", menu.getFatherId(), menu.getFullId())
			.extracting(Menu::getFatherId, Menu::getFullId)
			.containsExactly(Menu.ROOT_ID, "/" + menu.getId());

		// 配置mock对象，模拟找到子节点的情况
		// 有父节点的话,自己会成为父节点下的一个子节点
		Mockito.doReturn(new ArrayList<>(Collections.singletonList(new Menu(menuGateway)))).when(menuGateway).findLikeFullId(Mockito.anyString());
		// 移动到system节点下
		menu.moveFather(system.getId());
		// 验证菜单已经成为system节点的子节点
		assertThat(menu)
			.as("有父节点的话,自己会成为父节点下的一个子节点")
			.extracting(Menu::getFatherId, Menu::getFullId)
			.containsExactly(system.getId(), system.getFullId() + Menu.PATH_SEPARATOR + menu.getId());
	}

	/**
	 * 测试保存菜单功能
	 * 验证菜单保存时的各种场景：
	 * 1. 基本保存功能
	 * 2. 有父节点的菜单保存
	 * 3. 更新父节点后的菜单保存
	 */
	@Test
	void save() {
		// 测试基本保存功能
		menu.save();
		// 测试保存新创建的菜单
		new Menu(menuGateway).save();

		// 创建一个新的菜单并设置父节点为system
		Menu menu1 = new Menu(menuGateway);
		menu1.setId((long) UUID.randomUUID().hashCode());
		menu1.setFatherId(system.getId());
		menu1.save();
		// 验证全路径ID是否正确设置
		assertThat(menu1)
			.extracting(Menu::getFullId)
			.as("全路径id不能为空")
			.isNotNull()
			.as("验证全路径id是否在且挂在[system]节点下")
			.isEqualTo(system.getFullId() + Menu.PATH_SEPARATOR + menu1.getId());

		// 创建一个更新父节点的菜单
		Menu updateFatherId = new Menu(menuGateway);
		updateFatherId.setId(menu.getId());
		updateFatherId.setFatherId(system.getId());
		updateFatherId.save();
		// 验证更新父节点后全路径ID是否正确
		assertThat(updateFatherId)
			.extracting(Menu::getFullId)
			.as("全路径id不能为空")
			.isNotNull()
			.as("验证[menu]节点的全路径id是否在且挂在[system]节点下")
			.isEqualTo(system.getFullId() + Menu.PATH_SEPARATOR + updateFatherId.getId());
	}

	/**
	 * 测试删除菜单功能
	 * 验证菜单删除时的行为：
	 * 1. 正常删除无子节点的菜单
	 * 2. 尝试删除有子节点的菜单应抛出异常
	 */
	@Test
	void delete() {
		// 删除没有子节点的菜单
		menu.delete();

		// 配置mock对象，模拟菜单有子节点的情况
		Mockito.doReturn(Collections.singletonList(new Menu(menuGateway))).when(menuGateway).findLikeFullId(Mockito.anyString());
		// 尝试删除有子节点的菜单，应该抛出异常
		assertThatThrownBy(menu::delete)
			.isInstanceOf(Exception.class)
			.hasMessageContaining(ClientErrorCode.CHILDREN_IS_NOT_NULL.getMessage());
	}

	/**
	 * 测试查找子菜单功能
	 * 验证菜单能够正确查找并构建其子菜单树结构
	 */
	@Test
	void findChildrenTest() {
		// 创建根节点
		Menu root = new Menu(menuGateway);
		root.setId((long) UUID.randomUUID().hashCode());
		root.setFatherId(Menu.ROOT_ID);
		root.setFullId(Menu.PATH_SEPARATOR + root.getId());

		// 创建第一层子节点
		Menu node01 = new Menu(menuGateway);
		node01.setId((long) UUID.randomUUID().hashCode());
		node01.setFatherId(root.getId());
		node01.setFullId(root.getFullId() + Menu.PATH_SEPARATOR + node01.getId());

		Menu node02 = new Menu(menuGateway);
		node02.setId((long) UUID.randomUUID().hashCode());
		node02.setFatherId(root.getId());
		node02.setFullId(root.getFullId() + Menu.PATH_SEPARATOR + node02.getId());

		// 创建第二层子节点
		Menu node02_01 = new Menu(menuGateway);
		node02_01.setId((long) UUID.randomUUID().hashCode());
		node02_01.setFatherId(node02.getId());
		node02_01.setFullId(node02.getFullId() + Menu.PATH_SEPARATOR + node02_01.getId());

		Menu node01_01 = new Menu(menuGateway);
		node01_01.setId((long) UUID.randomUUID().hashCode());
		node01_01.setFatherId(node01.getId());
		node01_01.setFullId(node01.getFullId() + Menu.PATH_SEPARATOR + node01_01.getId());

		// 第一次查找子节点，应该返回空列表
		root.findChildren();
		assertThat(root)
			.extracting(Menu::getChildren)
			.asInstanceOf(InstanceOfAssertFactories.COLLECTION)
			.hasSize(0)
			.isEmpty();

		// 配置mock对象，返回所有子节点
		Mockito.doReturn(List.of(node01, node02, node02_01, node01_01)).when(menuGateway).findLikeFullId(root.getFullId());
		// 再次查找子节点，应该正确构建树形结构
		root.findChildren();
		Collection<Menu> children = root.getChildren();
		// 验证子节点是否正确构建
		for (Menu child : children) {
			assertThat(child)
				.extracting(Menu::getId)
				.isIn(node01.getId(), node02.getId());
			assertThat(child.getChildren())
				.hasSize(1);
		}
		// 输出根节点信息用于调试
		System.err.println(root);
	}
}
