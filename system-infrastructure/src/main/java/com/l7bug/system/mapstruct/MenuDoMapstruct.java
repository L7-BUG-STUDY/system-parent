package com.l7bug.system.mapstruct;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuType;
import com.l7bug.system.domain.menu.MetaVal;
import com.l7bug.system.mybatis.dataobject.SystemMenu;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.context.ApplicationContext;
import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;

/**
 * UserMapstruct
 *
 * @author Administrator
 * @since 2025/12/5 17:57
 */
@Slf4j
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MenuDoMapstruct {
	@Resource
	private ApplicationContext applicationContext;

	@Resource
	private JsonMapper jsonMapper;

	public Menu menu() {
		return applicationContext.getBean(Menu.class);
	}

	@Mapping(source = "type", target = "type", qualifiedByName = "mapType")
	@Mapping(source = "meta", target = "meta", qualifiedByName = "mapMetaVal")
	public abstract Menu mapDomain(SystemMenu menu);

	@Mapping(source = "type", target = "type", qualifiedByName = "mapTypeStr")
	@Mapping(source = "meta", target = "meta", qualifiedByName = "mapMetaJson")
	public abstract SystemMenu mapDo(Menu menu);

	@Named("mapMetaVal")
	protected MetaVal mapMetaVal(String val) {
		MetaVal metaVal;
		try {
			metaVal = jsonMapper.readValue(val, MetaVal.class);
		} catch (Exception e) {
			log.warn("错误的元数据:[{}]导致转换失败!转为默认空值!错误信息:[{}]", val, e.getMessage());
			metaVal = new MetaVal();
		}
		return metaVal;
	}

	@Named("mapMetaJson")
	protected String mapMetaJson(MetaVal val) {
		if (val == null) {
			return "{}";
		}
		try {
			return jsonMapper.writeValueAsString(val);
		} catch (Exception e) {
			log.warn("[{}]对象序列化json失败!置为空json字符串", val);
			log.warn("异常信息:", e);
			return "{}";
		}
	}

	@Named("mapTypeStr")
	protected String mapTypeStr(MenuType type) {
		return Optional.ofNullable(type).orElse(MenuType.FOLDER).name();
	}

	@Named("mapType")
	protected MenuType mapType(String type) {
		try {
			return MenuType.valueOf(Optional.ofNullable(type).orElse("FOLDER").toUpperCase());
		} catch (Exception e) {
			log.warn("传递错误的菜单类型导致转换失败,设为默认值:[{}],错误的类型:[{}],异常信息:[{}]", MenuType.FOLDER, type, e.getMessage());
			return MenuType.FOLDER;
		}
	}
}
