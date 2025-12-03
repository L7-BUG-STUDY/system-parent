package com.l7bug.system.domain.menu;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * MenuGateway
 *
 * @author Administrator
 * @since 2025/12/2 16:20
 */
public interface MenuGateway {
	/**
	 * 根据全路径id查询所有相关节点
	 *
	 * @param fullId 全路径id
	 * @return 全部子节点
	 */
	List<Menu> findByFullId(String fullId);

	/**
	 * 根据id查询菜单信息
	 *
	 * @param id id
	 * @return 菜单信息
	 */
	Menu findById(Long id);

	/**
	 * 获取所有根节点
	 *
	 * @return 所有的跟节点
	 */
	List<Menu> findAllRootNode();

	/**
	 * 根据id删除数据
	 *
	 * @param id id
	 * @return 根据id删除
	 */
	boolean deleteById(Long id);

	/**
	 * 保存数据
	 *
	 * @param menu 菜单
	 * @return true成功
	 */
	default boolean save(Menu menu) {
		return save(Collections.singletonList(menu));
	}

	/**
	 * 保存多条数据
	 *
	 * @param menus 菜单集合
	 * @return 操作是否成功
	 */
	boolean save(Collection<Menu> menus);
}
