package com.l7bug.system.client;

import com.l7bug.common.result.Result;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * MenuClient
 *
 * @author Administrator
 * @since 2025/12/3 18:00
 */
public interface MenuClient {

	Result<MenuNodeResponse> getRootNode();

	/**
	 * 根据节点id获取单条数据
	 *
	 * @param id 节点id
	 * @return 节点
	 */
	Result<MenuNodeResponse> getNodeById(@NotNull Long id);

	/**
	 * 新增菜单节点
	 *
	 * @param menuNodeRequest 菜单
	 * @return true新增成功
	 */
	Result<MenuNodeResponse> createMenuNode(@NotNull @Valid MenuNodeRequest menuNodeRequest);

	/**
	 * 根据id修改单条数据
	 *
	 * @param menuNodeRequest 菜单
	 * @param id              id
	 * @return true修改成功
	 */
	Result<Boolean> updateMenuNode(@NotNull @Valid MenuNodeRequest menuNodeRequest, @NotNull Long id);

	/**
	 * 根据id删除单条数据
	 *
	 * @param id id
	 * @return true成功
	 */
	Result<Boolean> deleteMenuNode(Long id);

	/**
	 * 添加排序值
	 *
	 * @param id  菜单id
	 * @param val 值
	 * @return 调用结果
	 */
	Result<Boolean> addSortVal(Long id, Integer val);
}
