package com.l7bug.system.client;

import com.l7bug.system.dto.response.MenuNodeResponse;

import java.util.List;

/**
 * MenuClient
 *
 * @author Administrator
 * @since 2025/12/3 18:00
 */
public interface MenuClient {
	/**
	 * 获取全部根节点以及子节点数据
	 *
	 * @return 根节点数据
	 */
	List<MenuNodeResponse> getAllRootNodes();
}
