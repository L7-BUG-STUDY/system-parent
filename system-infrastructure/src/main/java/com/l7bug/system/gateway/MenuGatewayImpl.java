package com.l7bug.system.gateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Strings;
import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuGateway;
import com.l7bug.system.mybatis.dataobject.SystemMenu;
import com.l7bug.system.mybatis.service.SystemMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

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

	@Override
	public List<Menu> findByFullId(String fullId) {
		if (Strings.isNullOrEmpty(fullId)) {
			return List.of();
		}
		List<SystemMenu> list = systemMenuService.list(Wrappers.lambdaQuery(SystemMenu.class));
		return List.of();
	}

	@Override
	public Menu findById(Long id) {
		return null;
	}

	@Override
	public boolean deleteById(Long id) {
		return false;
	}

	@Override
	public int save(Collection<Menu> menus) {
		return 0;
	}
}
