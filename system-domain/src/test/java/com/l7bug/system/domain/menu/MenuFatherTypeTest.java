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

	@Test
	void saveTest() {
		Menu page = new Menu(menuGateway);
		page.setName(UUID.randomUUID().toString());
		page.setType(MenuType.PAGE);
		page.setFatherId(root.getId());
		page.save();

		Menu button = new Menu(menuGateway);
		button.setName(UUID.randomUUID().toString());
		button.setType(MenuType.BUTTON);
		button.setFatherId(page.getId());
		button.save();
		Menu folder = new Menu(menuGateway);
		folder.setName(UUID.randomUUID().toString());
		folder.setType(MenuType.FOLDER);
		folder.setFatherId(root.getId());
		folder.save();

		Menu menu = new Menu(menuGateway);
		menu.setName(UUID.randomUUID().toString());
		menu.setType(MenuType.BUTTON);
		menu.setFatherId((long) UUID.randomUUID().toString().hashCode());
		Assertions.assertThatThrownBy(menu::save)
			.as("文件夹下添加按钮,一定会报错")
			.isInstanceOf(ClientException.class)
			.hasMessage(ClientErrorCode.FATHER_IS_NOT_PAGE.getMessage());
		menu.setFatherId(folder.getId());
		Assertions.assertThatThrownBy(menu::save)
			.as("文件夹下添加按钮,一定会报错")
			.isInstanceOf(ClientException.class)
			.hasMessage(ClientErrorCode.FATHER_IS_NOT_PAGE.getMessage());
		menu.setFatherId(page.getId());
		menu.setType(MenuType.PAGE);
		Assertions.assertThatThrownBy(menu::save)
			.as("页面下添加页面,一定会报错")
			.isInstanceOf(ClientException.class)
			.hasMessage(ClientErrorCode.FATHER_IS_NOT_FOLDER.getMessage());
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
