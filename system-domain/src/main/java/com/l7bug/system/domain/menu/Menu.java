package com.l7bug.system.domain.menu;

import lombok.Data;

import java.util.Map;

/**
 * 菜单
 *
 * @author Administrator
 * @since 2025/12/2 14:32
 */
@Data
public class Menu {
	private Long id;
	private Long fatherId;
	/**
	 * 全路径id
	 */
	private String fullId;
	/**
	 * 路径
	 */
	private String path;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 组件路径
	 */
	private String component;
	/**
	 * 菜单类型
	 */
	private MenuType type;
	/**
	 * 元数据
	 */
	private Map<String, Object> meta;
}
