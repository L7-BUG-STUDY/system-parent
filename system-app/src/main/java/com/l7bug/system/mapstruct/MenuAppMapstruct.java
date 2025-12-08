package com.l7bug.system.mapstruct;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
import jakarta.annotation.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.List;

/**
 * MenuConvertor
 *
 * @author l
 * @since 2025/12/6 12:14
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MenuAppMapstruct {
	@Resource
	private ApplicationContext applicationContext;

	public abstract MenuNodeResponse mapResponse(Menu menu);

	public abstract List<MenuNodeResponse> mapResponseByColletion(Collection<Menu> menus);

	public abstract Menu mapDomain(MenuNodeRequest menuNodeRequest);

	public Menu menu() {
		return applicationContext.getBean(Menu.class);
	}
}
