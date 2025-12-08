package com.l7bug.system.convertor;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MetaVal;
import com.l7bug.system.dto.base.MenuType;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
import com.l7bug.system.dto.response.Meta;
import com.l7bug.system.mapstruct.MenuMapstruct;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

	@Test
	void testNull() {
		List<MenuNodeResponse> menuNodeResponses = menuConvertor.mapResponseByColletion(null);
		assertThat((menuNodeResponses))
			.as("null转为null")
			.isNull();
		assertThat((menuConvertor.mapResponseByColletion(Collections.emptyList())))
			.as("空集合转为空集合")
			.isNotNull()
			.isEmpty();
		MenuNodeResponse menuNodeResponse = menuConvertor.mapResponse(null);
		assertThat((menuNodeResponse))
			.as("null转换为null")
			.isNull();
		assertThat(menuConvertor.mapDomain(null))
			.isNull();
	}

	@Test
	void testEnum() {
		MenuNodeRequest menuNodeRequest = new MenuNodeRequest();
		Menu menu = menuConvertor.mapDomain(menuNodeRequest);

		assertThat(menu)
			.as("空对象转换以后也是空对象")
			.isNotNull()
			.extracting(Menu::getType)
			.as("type值为null")
			.isNull();
		menuNodeRequest.setType(MenuType.PAGE);
		menu = menuConvertor.mapDomain(menuNodeRequest);
		assertThat(menu)
			.isNotNull()
			.extracting(Menu::getType)
			.as("type值转换以后,同样为领域type值")
			.isEqualTo(com.l7bug.system.domain.menu.MenuType.PAGE);
		menuNodeRequest = menuConvertor.mapResponse(menu);
		assertThat(menuNodeRequest.getType())
			.isNotNull()
			.as("domain转回request对象,type正常")
			.isEqualTo(MenuType.PAGE);
	}

	@Test
	void testMeta() {
		MenuNodeRequest menuNodeRequest = new MenuNodeRequest();
		Menu menu = menuConvertor.mapDomain(menuNodeRequest);
		assertThat(menu.getMeta())
			.as("null转null")
			.isNull();
		menuNodeRequest.setMeta(new Meta());
		menu = menuConvertor.mapDomain(menuNodeRequest);
		assertThat(menu.getMeta())
			.as("空值转空值,不为null")
			.isNotNull()
			.extracting(MetaVal::getIcon, MetaVal::getTitle)
			.as("属性值为null")
			.containsOnlyNulls()
		;
		menuNodeRequest.getMeta().setIcon(UUID.randomUUID().toString());
		menuNodeRequest.getMeta().setTitle(Collections.emptyMap());
		menu = menuConvertor.mapDomain(menuNodeRequest);
		assertThat(menu.getMeta())
			.as("验证图标不为空,且值匹配")
			.isNotNull()
			.extracting(MetaVal::getIcon)
			.isNotNull()
			.isEqualTo(menuNodeRequest.getMeta().getIcon());
		assertThat(menu.getMeta())
			.as("验证标题为空map")
			.isNotNull()
			.extracting(MetaVal::getTitle)
			.asInstanceOf(InstanceOfAssertFactories.MAP)
			.isNotNull()
			.isEmpty();
		menuNodeRequest.getMeta().setTitle(Map.of("zh_cn", "中文", "us_en", "英文"));
		menu = menuConvertor.mapDomain(menuNodeRequest);
		assertThat(menu.getMeta())
			.isNotNull()
			.extracting(MetaVal::getTitle)
			.asInstanceOf(InstanceOfAssertFactories.MAP)
			.as("map转换不能失败")
			.isNotNull()
			.as("大小预设的2,转换后也应该一致")
			.hasSize(2)
			.as("value值必须一致")
			.containsValues("中文", "英文")
			.as("key值一致")
			.containsKeys("zh_cn", "us_en");
	}
}
