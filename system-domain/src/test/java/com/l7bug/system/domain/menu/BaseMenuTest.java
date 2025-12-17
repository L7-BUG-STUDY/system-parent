package com.l7bug.system.domain.menu;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * BaseMenuTest
 *
 * @author Administrator
 * @since 2025/12/17 12:09
 */
public abstract class BaseMenuTest {
	protected MenuGateway menuGateway;

	@BeforeEach
	void setUpBaseMenuTest() {
		menuGateway = Mockito.mock(MenuGateway.class);
		Map<Long, Menu> menuMap = new HashMap<>();
		Mockito.doAnswer(item -> {
				Long argument = item.getArgument(0, Long.class);
				return menuMap.get(argument);
			})
			.when(menuGateway).findById(Mockito.anyLong());
		Mockito.doAnswer(item -> {
			Collection<Menu> args0 = item.getArgument(0);
			for (Menu menu1 : args0) {
				if (menu1.getId() == null) {
					menu1.setId((long) UUID.randomUUID().hashCode());
				}
				menuMap.put(menu1.getId(), menu1);
			}
			return true;
		}).when(menuGateway).save(Mockito.anyCollection());
	}
}
