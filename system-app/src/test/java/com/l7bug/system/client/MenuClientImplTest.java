package com.l7bug.system.client;


import com.l7bug.common.result.Result;
import com.l7bug.system.dao.mapstruct.MenuDoMapstruct;
import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuGateway;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuClientImplTest {
	@Autowired
	private MenuClientImpl menuClient;
	@Autowired
	private JsonMapper jsonMapper;

	@Autowired
	private MenuDoMapstruct menuDoMapstruct;
	@Autowired
	private MenuGateway menuGateway;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getRootNode() {
		Result<MenuNodeResponse> rootNode = menuClient.getRootNode();
		assertThat(rootNode)
			.isNotNull()
			.extracting(Result::getData)
			.isNotNull()
			.extracting(MenuNodeResponse::getId)
			.as("查询出来的节点必定是root节点")
			.isEqualTo(Menu.ROOT_ID);
		System.err.println(jsonMapper.writeValueAsString(rootNode));

	}

	@Test
	void getNodeById() {
		Menu menu = menuDoMapstruct.menu();
		menu.setName(UUID.randomUUID().toString());
		menu.save();
		Result<MenuNodeResponse> nodeById = menuClient.getNodeById(menu.getId());
		Assertions.assertNotNull(nodeById);
		Assertions.assertTrue(nodeById.isSuccess());
		Assertions.assertEquals(menu.getId(), nodeById.getData().getId());
		Assertions.assertEquals(menu.getName(), nodeById.getData().getName());
		menu.delete();
	}

	@Test
	void createMenuNode() {
		MenuNodeRequest menuNodeRequest = new MenuNodeRequest();
		menuNodeRequest.setName(UUID.randomUUID().toString());
		Result<MenuNodeResponse> menuNode = menuClient.createMenuNode(menuNodeRequest);
		Assertions.assertEquals(menuNode.getData().getName(), menuNodeRequest.getName());
		Result<MenuNodeResponse> nodeById = menuClient.getNodeById(menuNode.getData().getId());
		Assertions.assertNotNull(nodeById);
		menuClient.deleteMenuNode(menuNode.getData().getId());
	}

	@Test
	void updateMenuNode() {
		MenuNodeRequest menuNodeRequest = new MenuNodeRequest();
		Result<Boolean> booleanResult = menuClient.updateMenuNode(menuNodeRequest, System.currentTimeMillis());
		Assertions.assertFalse(booleanResult.getData());
		Menu menu = menuDoMapstruct.menu();
		menu.setName(UUID.randomUUID().toString());
		menu.save();
		menuNodeRequest.setName(UUID.randomUUID().toString());
		menuClient.updateMenuNode(menuNodeRequest, menu.getId());
		menu = menuGateway.findById(menu.getId());
		Assertions.assertEquals(menu.getName(), menuNodeRequest.getName());
		menu.delete();
	}

	@Test
	void deleteMenuNode() {
		Menu menu = menuDoMapstruct.menu();
		menu.setName(UUID.randomUUID().toString());
		menu.save();
		Result<Boolean> booleanResult = menuClient.deleteMenuNode(menu.getId());
		assertThat(booleanResult)
			.as("调用结果不能为空")
			.isNotNull()
			.extracting(Result::getData)
			.as("调用结果必须为true")
			.isEqualTo(true);
		booleanResult = menuClient.deleteMenuNode((long) UUID.randomUUID().hashCode());
		assertThat(booleanResult)
			.isNotNull()
			.extracting(Result::getData)
			.isEqualTo(false);
		menu = menuGateway.findById(menu.getId());
		assertThat(menu)
			.isNull();
	}

	@Test
	void addSortVal() {
		// 测试菜单不存在的情况
		Result<Boolean> result1 = menuClient.addSortVal((long) UUID.randomUUID().hashCode(), 5);
		assertThat(result1)
			.isNotNull()
			.extracting(Result::isSuccess, Result::getData)
			.containsExactly(true, false);

		// 测试菜单存在的情况
		Menu menu = menuDoMapstruct.menu();
		menu.setName(UUID.randomUUID().toString());
		menu.save();

		// 验证正常调用
		Result<Boolean> result2 = menuClient.addSortVal(menu.getId(), 5);
		assertThat(result2)
			.isNotNull()
			.extracting(Result::isSuccess, Result::getData)
			.containsExactly(true, true);

		// 清理测试数据
		menu.delete();
	}
}
