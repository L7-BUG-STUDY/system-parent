package com.l7bug.system.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuType;
import com.l7bug.system.domain.menu.MetaVal;
import com.l7bug.system.mybatis.dataobject.SystemMenu;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * MenuConvertor
 *
 * @author Administrator
 * @since 2025/11/7 12:14
 */
@Slf4j
@AllArgsConstructor
@Component
public class MenuConvertor {
	private final ApplicationContext applicationContext;
	private final ObjectMapper objectMapper;

	public Menu mapDomain(SystemMenu systemMenu) {
		Menu menu = applicationContext.getBean(Menu.class);
		BeanUtils.copyProperties(systemMenu, menu);
		try {
			menu.setMeta(objectMapper.readValue(systemMenu.getMeta(), MetaVal.class));
		} catch (JsonProcessingException e) {
			log.warn("错误的元数据:[{}]导致转换失败!转为默认空值!错误信息:[{}]", systemMenu.getMeta(), e.getMessage());
			menu.setMeta(new MetaVal());
		}
		try {
			menu.setType(MenuType.valueOf(systemMenu.getType().toUpperCase()));
		} catch (IllegalArgumentException e) {
			log.warn("传递错误的菜单类型导致转换失败,设为默认值:[{}],错误的类型:[{}],异常信息:[{}]", MenuType.FOLDER, systemMenu.getType(), e.getMessage());
			menu.setType(MenuType.FOLDER);
		}
		return menu;
	}

	public SystemMenu mapDo(Menu menu) {
		if (menu == null) {
			return null;
		}
		SystemMenu systemMenu = new SystemMenu();
		BeanUtils.copyProperties(menu, systemMenu);
		try {
			systemMenu.setMeta(objectMapper.writeValueAsString(menu.getMeta()));
		} catch (JsonProcessingException e) {
			log.warn("[{}]对象序列化json失败!置为空json字符串", Menu.class);
			log.warn("异常信息:", e);
			systemMenu.setMeta("{}");
		}
		systemMenu.setType(Optional.ofNullable(menu.getType()).orElse(MenuType.FOLDER).name());
		return systemMenu;
	}
}
