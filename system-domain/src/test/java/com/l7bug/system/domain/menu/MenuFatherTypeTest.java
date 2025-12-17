package com.l7bug.system.domain.menu;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * MenuFatherTypeTest
 *
 * @author Administrator
 * @since 2025/12/17 12:11
 */
public class MenuFatherTypeTest extends BaseMenuTest {
	private Menu root;

	@BeforeEach
	void setUp() {
		root = new Menu(menuGateway);
		root.setName(UUID.randomUUID().toString());
		root.setType(MenuType.FOLDER);
		root.save();
	}

	/**
	 * 测试菜单保存功能及其父子关系约束
	 * 验证不同类型菜单之间的父子关系约束规则：
	 * 1. 页面(PAGE)只能作为根节点或文件夹(FOLDER)的子节点
	 * 2. 按钮(BUTTON)只能作为页面(PAGE)的子节点
	 * 3. 文件夹(FOLDER)只能作为根节点或文件夹(FOLDER)的子节点
	 */
	@Test
	void saveTest() {
		// 创建一个页面菜单并保存，父节点为根节点
		Menu page = new Menu(menuGateway);
		page.setName(UUID.randomUUID().toString());
		page.setType(MenuType.PAGE);
		page.setFatherId(root.getId());
		page.save();

		// 创建一个按钮菜单并保存，父节点为页面
		Menu button = new Menu(menuGateway);
		button.setName(UUID.randomUUID().toString());
		button.setType(MenuType.BUTTON);
		button.setFatherId(page.getId());
		button.save();
		
		// 创建一个文件夹菜单并保存，父节点为根节点
		Menu folder = new Menu(menuGateway);
		folder.setName(UUID.randomUUID().toString());
		folder.setType(MenuType.FOLDER);
		folder.setFatherId(root.getId());
		folder.save();

		// 测试各种非法的父子关系组合
		Menu menu = new Menu(menuGateway);
		menu.setName(UUID.randomUUID().toString());
		menu.setType(MenuType.BUTTON);
		
		// 尝试将按钮添加到不存在的节点下，应该抛出异常
		menu.setFatherId((long) UUID.randomUUID().toString().hashCode());
		Assertions.assertThatThrownBy(menu::save)
			.as("文件夹下添加按钮,一定会报错")
			.isInstanceOf(ClientException.class)
			.hasMessage(ClientErrorCode.FATHER_IS_NOT_PAGE.getMessage());
			
		// 尝试将按钮添加到文件夹下，应该抛出异常
		menu.setFatherId(folder.getId());
		Assertions.assertThatThrownBy(menu::save)
			.as("文件夹下添加按钮,一定会报错")
			.isInstanceOf(ClientException.class)
			.hasMessage(ClientErrorCode.FATHER_IS_NOT_PAGE.getMessage());
			
		// 尝试将页面添加到页面下，应该抛出异常
		menu.setFatherId(page.getId());
		menu.setType(MenuType.PAGE);
		Assertions.assertThatThrownBy(menu::save)
			.as("页面下添加页面,一定会报错")
			.isInstanceOf(ClientException.class)
			.hasMessage(ClientErrorCode.FATHER_IS_NOT_FOLDER.getMessage());
			
		// 尝试将文件夹添加到页面下，应该抛出异常
		menu.setFatherId(page.getId());
		menu.setType(MenuType.FOLDER);
		Assertions.assertThatThrownBy(menu::save)
			.as("页面下添加文件夹,一定会报错")
			.isInstanceOf(ClientException.class)
			.hasMessage(ClientErrorCode.FATHER_IS_NOT_FOLDER.getMessage());
	}

	@Test
	void typeIsNull() {
		Menu menu = new Menu(menuGateway);
		menu.setType(null);
		menu.setName(UUID.randomUUID().toString());
		menu.save();
	}
}
