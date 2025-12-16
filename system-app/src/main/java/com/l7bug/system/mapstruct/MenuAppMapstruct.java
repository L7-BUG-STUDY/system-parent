package com.l7bug.system.mapstruct;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
import jakarta.annotation.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * MenuConvertor
 *
 * @author l
 * @since 2025/12/6 12:14
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MenuAppMapstruct {
	@Resource
	private ApplicationContext applicationContext;

	public abstract MenuNodeResponse mapResponse(Menu menu);

	/**
	 * 将Menu队列转换为MenuNodeResponse列表
	 *
	 * @param menus Menu队列，通常是一个已排序的PriorityQueue
	 * @return MenuNodeResponse列表
	 */
	public List<MenuNodeResponse> mapResponseByQueue(Queue<Menu> menus) {
		if (menus == null) {
			return new ArrayList<>();
		}
		// 创建响应对象列表
		var menuNodeResponses = new ArrayList<MenuNodeResponse>(menus.size());
		// 创建临时队列以避免修改原始队列，并保持队列的有序性
		Queue<Menu> temp = new LinkedList<>(menus);
		// 使用poll()方式按队列顺序逐个处理元素，确保维持原有排序
		while (!temp.isEmpty()) {
			menuNodeResponses.add(mapResponse(temp.poll()));
		}
		return menuNodeResponses;
	}

	public abstract Menu mapDomain(MenuNodeRequest menuNodeRequest);

	public Menu menu() {
		return applicationContext.getBean(Menu.class);
	}
}
