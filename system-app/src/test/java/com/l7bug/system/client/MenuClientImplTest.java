package com.l7bug.system.client;


import com.l7bug.common.result.Result;
import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuGateway;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
import com.l7bug.system.mapstruct.MenuMapstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class MenuClientImplTest {
	@Autowired
	private MenuClientImpl menuClient;
	@Autowired
	private JsonMapper jsonMapper;

	@Autowired
	private MenuMapstruct menuMapstruct;
	@Autowired
	private MenuGateway menuGateway;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getAllRootNodes() {
		Result<List<MenuNodeResponse>> allRootNodes = menuClient.getAllRootNodes();
		Assertions.assertNotNull(allRootNodes);
		Assertions.assertTrue(allRootNodes.isSuccess());
		Assertions.assertNotNull(allRootNodes.getData());
		System.err.println(jsonMapper.writeValueAsString(allRootNodes));

	}

	@Test
	void getNodeById() {
		Menu menu = menuMapstruct.menu();
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
		Menu menu = menuMapstruct.menu();
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
		Menu menu = menuMapstruct.menu();
		menu.setName(UUID.randomUUID().toString());
		menu.save();
		Result<Boolean> booleanResult = menuClient.deleteMenuNode(menu.getId());
		Assertions.assertFalse(booleanResult.getData());
		menu = menuGateway.findById(menu.getId());
		Assertions.assertNull(menu);
	}
}
