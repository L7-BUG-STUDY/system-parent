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
	 * 根据全路径ID查找相似的菜单列表
	 * <p>
	 * 该方法用于查找具有指定全路径ID前缀的菜单项。如果提供了fullId参数，
	 * 则查找全路径ID以该参数加上路径分隔符开头的菜单项；
	 * 如果未提供fullId参数或参数为空，则返回所有菜单项。
	 *
	 * @param fullId 菜单全路径ID前缀，用于模糊匹配菜单项
	 * @return List<Menu> 符合条件的菜单领域对象列表
	 */
	List<Menu> findLikeFullId(String fullId);

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
	Collection<Menu> findAllRootNode();

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
