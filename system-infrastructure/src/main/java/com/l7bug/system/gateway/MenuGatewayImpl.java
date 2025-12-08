package com.l7bug.system.gateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuGateway;
import com.l7bug.system.mapstruct.MenuDoMapstruct;
import com.l7bug.system.mybatis.dataobject.SystemMenu;
import com.l7bug.system.mybatis.service.SystemMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RuleGatewayImpl
 *
 * @author Administrator
 * @since 2025/12/2 18:10
 */
@AllArgsConstructor
@Component
public class MenuGatewayImpl implements MenuGateway {
	private final SystemMenuService systemMenuService;
	private final MenuDoMapstruct menuDoMapstruct;

	@Override
	public List<Menu> findByFullId(String fullId) {
		if (fullId == null) {
			return new ArrayList<>();
		}
		List<SystemMenu> list = systemMenuService.list(Wrappers.lambdaQuery(SystemMenu.class).likeRight(SystemMenu::getFullId, fullId + Menu.PATH_SEPARATOR).orderByAsc(SystemMenu::getSort));
		return list.stream().map(menuDoMapstruct::mapDomain).toList();
	}

	@Override
	public Menu findById(Long id) {
		return systemMenuService.getOptById(id)
			.map(menuDoMapstruct::mapDomain)
			.orElse(null);
	}

	@Override
	public List<Menu> findAllRootNode() {
		Menu menu = menuDoMapstruct.menu();
		menu.setId(-1L);
		menu.setFullId("");
		menu.setName("root");
		menu.findChildren();
		return menu.getChildren();
	}

	@Override
	public boolean deleteById(Long id) {
		return systemMenuService.removeById(id);
	}

	@Override
	public boolean save(Collection<Menu> menus) {
		Map<Menu, SystemMenu> collect = Optional.ofNullable(menus)
			.orElse(Collections.emptyList())
			.stream()
			.collect(Collectors.toMap(menu -> menu, menuDoMapstruct::mapDo));
		boolean b = systemMenuService.saveOrUpdateBatch(collect.values());
		for (Map.Entry<Menu, SystemMenu> entry : collect.entrySet()) {
			entry.getKey().setId(entry.getValue().getId());
		}
		return b;
	}
}
