package com.l7bug.system.domain.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Menu类nodeAddSortVal方法的单元测试
 * 测试修改节点排序值后自动重新排序的功能
 */
class MenuNodeAddSortValTest {

	private MenuGateway menuGateway;
	private Menu fatherMenu;
	private Menu child1;
	private Menu child2;
	private Menu child3;

	@BeforeEach
	void setUp() {
		menuGateway = Mockito.mock(MenuGateway.class);

		// 创建父菜单
		fatherMenu = new Menu(menuGateway);
		fatherMenu.setId(1L);
		fatherMenu.setFatherId(Menu.ROOT_ID);
		fatherMenu.setFullId("/1");

		// 创建子菜单
		child1 = new Menu(menuGateway);
		child1.setId(2L);
		child1.setFatherId(1L);
		child1.setFullId("/1/2");
		child1.setSort(10);

		child2 = new Menu(menuGateway);
		child2.setId(3L);
		child2.setFatherId(1L);
		child2.setFullId("/1/3");
		child2.setSort(5);

		child3 = new Menu(menuGateway);
		child3.setId(4L);
		child3.setFatherId(1L);
		child3.setFullId("/1/4");
		child3.setSort(15);
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
		fatherMenu.save();
		child1.save();
		child2.save();
		child3.save();
	}

	/**
	 * 测试减少节点排序值并重新排序的功能
	 */
	@Test
	void nodeAddSortVal_withNegativeValue_shouldUpdateSortValuesAndReorder() {
		// 准备子菜单列表
		List<Menu> siblingMenus = Arrays.asList(child1, child2, child3);
		when(menuGateway.findByFatherId(1L)).thenReturn(siblingMenus);

		// 调用待测试的方法，将child1的sort值减少8 (从10变为2)
		child1.nodeAddSortVal(-8);

		// 验证child1的sort值已被更新
		assertThat(child1.getSort()).isEqualTo(0);

		assertThat(child1.getSort()).isEqualTo(0);
		assertThat(child2.getSort()).isEqualTo(2);
		assertThat(child3.getSort()).isEqualTo(4);
	}

	/**
	 * 测试只有一个子节点的情况
	 */
	@Test
	void nodeAddSortVal_withSingleChild_shouldUpdateSortValue() {
		// 准备只有一个子菜单的列表
		List<Menu> siblingMenus = Collections.singletonList(child1);
		when(menuGateway.findByFatherId(1L)).thenReturn(siblingMenus);

		// 调用待测试的方法
		child1.nodeAddSortVal(5);

		// 验证child1的sort值已被更新
		assertThat(child1.getSort()).isEqualTo(0);
	}
}
