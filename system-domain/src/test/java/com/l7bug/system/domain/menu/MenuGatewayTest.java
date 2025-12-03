package com.l7bug.system.domain.menu;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

class MenuGatewayTest {
	@Test
	void test() {
		MenuGateway menuGateway = new MenuGateway() {
			@Override
			public List<Menu> findByFullId(String fullId) {
				return List.of();
			}

			@Override
			public Menu findById(Long id) {
				return null;
			}

			@Override
			public List<Menu> findAllRootNode() {
				return List.of();
			}

			@Override
			public boolean deleteById(Long id) {
				return false;
			}

			@Override
			public boolean save(Collection<Menu> menus) {
				return false;
			}
		};
		menuGateway.save(new Menu(null));
	}
}
