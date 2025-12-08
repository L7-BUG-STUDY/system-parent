package com.l7bug.system.convertor;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.dto.response.MenuNodeResponse;
import com.l7bug.system.mapstruct.MenuMapstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

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
		// 1. 断言根节点属性
		assertThat(menuNodeResponse)
			.isNotNull() // 确保对象非空
			.extracting(MenuNodeResponse::getId, MenuNodeResponse::getName)
			.containsExactly(root.getId(), root.getName());

		// 2. 断言子节点列表 (Children)
		assertThat(menuNodeResponse.getChildren())
			.isNotNull()
			.hasSize(root.getChildren().size()); // 断言大小是否匹配

		// 3. 断言第一层子节点的 ID
		// 提取子节点列表中的第一个元素，并断言其 ID
		assertThat(menuNodeResponse.getChildren().get(0).getId())
			.as("Checking first child ID") // 添加一个描述信息，失败时更有帮助
			.isEqualTo(root.getChildren().get(0).getId());

		// 4. 断言第二层（孙）节点的 ID
		// 提取第一层子节点（索引 0）的 Children 列表中的第一个元素，并断言其 ID
		assertThat(menuNodeResponse.getChildren().get(0).getChildren().get(0).getId())
			.as("Checking grandchild ID")
			.isEqualTo(root.getChildren().get(0).getChildren().get(0).getId());
		System.err.println(jsonMapper.writeValueAsString(menuNodeResponse));
		String content = "Hello AssertJ World!";

		// 1. 断言包含
		assertThat(content)
			.as("包含AssertJ")
			.contains("AssertJ")
			.startsWith("Hello").endsWith("World!");
	}
}
