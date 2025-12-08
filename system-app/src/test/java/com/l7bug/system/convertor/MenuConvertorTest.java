package com.l7bug.system.convertor;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.dto.response.MenuNodeResponse;
import com.l7bug.system.mapstruct.MenuMapstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
public class MenuConvertorTest {
	@Autowired
	private MenuConvertor menuConvertor;
	@Autowired
	private MenuMapstruct menuMapstruct;

	@Autowired
	private JsonMapper jsonMapper;

	@BeforeEach
	void setUp() {
		System.err.println(menuConvertor);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	public void mapResponse() {
		Menu root = menuMapstruct.menu();
		root.setName("root");
		root.save();
		Menu children = menuMapstruct.menu();
		children.setName("children");
		root.addChildren(children);
		Menu temp = menuMapstruct.menu();
		temp.setName("temp");
		children.addChildren(temp);
		root.findChildren();
		MenuNodeResponse menuNodeResponse = menuConvertor.mapResponse(root);
		Assertions.assertEquals(root.getId(), menuNodeResponse.getId());
		Assertions.assertEquals(root.getName(), menuNodeResponse.getName());
		Assertions.assertEquals(root.getChildren().size(), menuNodeResponse.getChildren().size());
		Assertions.assertEquals(root.getChildren().get(0).getId(), menuNodeResponse.getChildren().get(0).getId());
		Assertions.assertEquals(root.getChildren().get(0).getChildren().get(0).getId(), menuNodeResponse.getChildren().get(0).getChildren().get(0).getId());
		System.err.println(jsonMapper.writeValueAsString(menuNodeResponse));
	}
}
