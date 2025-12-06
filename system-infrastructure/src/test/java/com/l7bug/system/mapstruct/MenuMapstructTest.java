package com.l7bug.system.mapstruct;

import com.google.common.base.Strings;
import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuType;
import com.l7bug.system.domain.menu.MetaVal;
import com.l7bug.system.mybatis.dataobject.SystemMenu;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.lang.reflect.Type;

@Slf4j
@SpringBootTest
class MenuMapstructTest {
	private final MetaVal exceptionMeta = new MetaVal();
	MetaVal metaVal = new MetaVal();
	@MockitoSpyBean
	private JsonMapper objectMapper;
	@Autowired
	private MenuMapstruct menuMapstruct;

	@BeforeEach
	void setUp() throws Exception {
		Mockito.doThrow(new RuntimeException("")).when(objectMapper).writeValueAsString(exceptionMeta);
		ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
		Mockito.doReturn(new Menu(Mockito.mock())).when(applicationContext).getBean(Menu.class);
		metaVal.setIcon("icon.png");
		metaVal.setTitle(objectMapper.readValue("""
			{
				"zh_CN": "系统管理",
				"en_US": "系统管理-en"
			}
			""", new TypeReference<>() {
			@Override
			public Type getType() {
				return super.getType();
			}
		}));
	}

	@Test
	void mapDomain() throws Exception {
		SystemMenu systemMenu = new SystemMenu();
		Menu menu = menuMapstruct.mapDomain(null);
		Assertions.assertNull(menu);
		menu = menuMapstruct.mapDomain(systemMenu);
		Assertions.assertNotNull(menu);
		Assertions.assertEquals(MenuType.FOLDER, menu.getType());
		Assertions.assertNotNull(menu.getMeta());
		Assertions.assertNull(menu.getMeta().getIcon());
		Assertions.assertNull(menu.getMeta().getTitle());
		systemMenu.setMeta(objectMapper.writeValueAsString(metaVal));
		systemMenu.setType(MenuType.PAGE.name());
		menu = menuMapstruct.mapDomain(systemMenu);
		Assertions.assertEquals(MenuType.PAGE, menu.getType());
		systemMenu.setType(MenuType.FOLDER.name());
		menu = menuMapstruct.mapDomain(systemMenu);
		Assertions.assertEquals(MenuType.FOLDER, menu.getType());
		Assertions.assertFalse(menu.getMeta().getTitle().isEmpty());
		Assertions.assertEquals(2, menu.getMeta().getTitle().size());
		Assertions.assertEquals("系统管理", menu.getMeta().getTitle().get("zh_CN"));
		Assertions.assertEquals("icon.png", menu.getMeta().getIcon());
	}

	@Test
	void mapDo() {
		SystemMenu systemMenu = menuMapstruct.mapDo(null);
		Assertions.assertNull(systemMenu);
		Menu menu = new Menu(Mockito.mock());
		menu.setMeta(metaVal);
		menu.setType(MenuType.PAGE);
		systemMenu = menuMapstruct.mapDo(menu);
		log.info(systemMenu.toString());
		Assertions.assertNotNull(systemMenu);
		Assertions.assertNotNull(systemMenu.getType());
		Assertions.assertNotNull(systemMenu.getMeta());
		Assertions.assertEquals(MenuType.PAGE.name(), systemMenu.getType());
		Assertions.assertFalse(Strings.isNullOrEmpty(systemMenu.getMeta()));
		menu.setType(MenuType.FOLDER);
		systemMenu = menuMapstruct.mapDo(menu);
		Assertions.assertEquals(MenuType.FOLDER.name(), systemMenu.getType());
		menu.setMeta(null);
		menu.setType(null);
		systemMenu = menuMapstruct.mapDo(menu);
		Assertions.assertNotNull(systemMenu);
		Assertions.assertNotNull(systemMenu.getType());
		Assertions.assertNotNull(systemMenu.getMeta());
		Assertions.assertEquals(MenuType.FOLDER.name(), systemMenu.getType());
		Assertions.assertFalse(Strings.isNullOrEmpty(systemMenu.getMeta()));
		Assertions.assertFalse(systemMenu.getMeta().isBlank());
		menu.setMeta(exceptionMeta);
		systemMenu = menuMapstruct.mapDo(menu);
		Assertions.assertEquals("{}", systemMenu.getMeta());
	}

	@Test
	void mapType() {
		MenuType menuType = menuMapstruct.mapType("123");
		Assertions.assertEquals(MenuType.FOLDER, menuType);
	}
}
