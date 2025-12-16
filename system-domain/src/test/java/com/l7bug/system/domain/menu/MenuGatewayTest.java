package com.l7bug.system.domain.menu;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

/**
 * 菜单网关测试类
 * <p>
 * 用于测试MenuGateway接口的基本功能
 */
class MenuGatewayTest {

	/**
	 * 测试菜单网关的基本功能
	 * 创建一个MenuGateway的匿名实现，并调用save方法保存一个新的菜单对象
	 */
	@Test
	void test() {
		// 创建MenuGateway的匿名实现类，重写所有方法并返回默认值
		MenuGateway menuGateway = new MenuGateway() {
			@Override
			public List<Menu> findLikeFullId(String fullId) {
				// 返回空列表作为默认实现
				return List.of();
			}

			@Override
			public Menu findById(Long id) {
				// 返回null作为默认实现
				return null;
			}

			@Override
			public Collection<Menu> findByFatherId(Long fatherId) {
				return List.of();
			}

			@Override
			public Collection<Menu> findAllRootNode() {
				// 返回空列表作为默认实现
				return List.of();
			}

			@Override
			public boolean deleteById(Long id) {
				// 返回false作为默认实现
				return false;
			}

			@Override
			public boolean save(Collection<Menu> menus) {
				// 返回false作为默认实现
				return false;
			}
		};
		// 调用save方法保存一个新的菜单对象
		menuGateway.save(new Menu(null));
	}
}
